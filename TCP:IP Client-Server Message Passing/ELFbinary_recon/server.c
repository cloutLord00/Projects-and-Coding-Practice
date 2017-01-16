/*
** server.c -- a stream socket server demo
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/wait.h>
#include <signal.h>

#include <sys/stat.h>
#include <fcntl.h>

#define PORT "3506"  // the port users will be connecting to
#define MAXDATASIZE 100
#define BACKLOG 10	 // how many pending connections queue will hold

typedef struct {
	char name[MAXDATASIZE];
	unsigned short ABI;
	unsigned int text;
	unsigned int data_sz;
	unsigned int bss_sz;
	unsigned int entry;
	unsigned int checksum;
} ELF_data;

static const char *dataKEY = "4$niY8R3sP1QwErTY4";
static const char *mKEY = "$&YCR3s$*#:;[}'/@~";

//return true (1) if parity check passes, false (0) otherwise
static int checkSum( ELF_data *data ){
	unsigned int i,sum,len;
	if( !data ){
		return 0;
	}
	sum = data->name[0];
	len = strlen(data->name);
	for(i = 1; i < len; i++){
		sum += data->name[i];
	}
	sum += (unsigned short) data->ABI;
	sum += (unsigned int) data->text;
	sum += (unsigned int) data->data_sz;
	sum += (unsigned int) data->bss_sz;
	sum += (unsigned int) data->entry;

	if( sum == data->checksum ){
		return 1;
	}else{
		return 0;
	}
}

static void hideMessage( char *message ){
	if( message ){
		unsigned int i,len = strlen(message);
		for(i = 0; i < len; i++){
			message[i] ^= mKEY[i%18];
		}
	}
}

static void recoverMessage( char *message ){
	if( message ){
		unsigned int i,len = strlen(message);
		for(i = 0; i < len; i++){
			message[i] ^= mKEY[i%18];
		}
	}
}



static void decDATA( ELF_data *data ){
	if( data ){
		int i,val = 0,len = strlen(data->name);
		for(i = 0; i < len; i++){
			data->name[i] ^= dataKEY[i%18];
			val += dataKEY[i%18];
		}
		data->ABI ^= (unsigned short) val;
		data->text ^= (unsigned int) val;
		data->data_sz ^= (unsigned int) val;
		data->bss_sz ^= (unsigned int) val;
		data->entry ^= (unsigned int) val;
		data->checksum ^= (unsigned int) val;
	}
}

extern int AddValues(int a, int b);

void sigchld_handler(int s)
{
	// waitpid() might overwrite errno, so we save and restore it:
	int saved_errno = errno;

	while(waitpid(-1, NULL, WNOHANG) > 0);

	errno = saved_errno;
}


// get sockaddr, IPv4 or IPv6:
void *get_in_addr(struct sockaddr *sa)
{
	if (sa->sa_family == AF_INET) {
		return &(((struct sockaddr_in*)sa)->sin_addr);
	}

	return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

int main(void)
{
	int verifyAdmin = 0;
	int sockfd, new_fd;  // listen on sock_fd, new connection on new_fd
	int handshake = ((int) &sockfd) *100 %2379, validate_handshake = handshake;
	struct addrinfo hints, *servinfo, *p;
	struct sockaddr_storage their_addr; // connector's address information
	socklen_t sin_size;
	struct sigaction sa;
	int yes=1, admin = 0;
	char s[INET6_ADDRSTRLEN], buffer[sizeof(ELF_data)];
	char filename[MAXDATASIZE];
	int rv, size, authenticate;
	char message[MAXDATASIZE];
	ELF_data bin_data;
	FILE *fp, *fpp;
	char fname[MAXDATASIZE];
	char infotag[5];
	infotag[0] = 'i';
	infotag[1] = 'n';
	infotag[2] = 'f';
	infotag[3] = 'o';
	infotag[4] = '\0';

	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;
	hints.ai_flags = AI_PASSIVE; // use my IP

	//printf("Assembly function: %d + %d + 5 = %d\n", 1, 2, AddValues(1, 2));

	if ((rv = getaddrinfo(NULL, PORT, &hints, &servinfo)) != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
		return 1;
	}

	// loop through all the results and bind to the first we can
	for(p = servinfo; p != NULL; p = p->ai_next) {
		if ((sockfd = socket(p->ai_family, p->ai_socktype,
				p->ai_protocol)) == -1) {
			perror("server: socket");
			continue;
		}

		if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &yes,
				sizeof(int)) == -1) {
			perror("setsockopt");
			exit(1);
		}

		if (bind(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
			close(sockfd);
			perror("server: bind");
			continue;
		}

		break;
	}

	freeaddrinfo(servinfo); // all done with this structure

	if (p == NULL)  {
		fprintf(stderr, "server: failed to bind\n");
		exit(1);
	}

	if (listen(sockfd, BACKLOG) == -1) {
		perror("listen");
		exit(1);
	}

	sa.sa_handler = sigchld_handler; // reap all dead processes
	sigemptyset(&sa.sa_mask);
	sa.sa_flags = SA_RESTART;
	if (sigaction(SIGCHLD, &sa, NULL) == -1) {
		perror("sigaction");
		exit(1);
	}

	printf("server: waiting for connections...\n");

	while(1){  // main accept() loop
		sin_size = sizeof their_addr;
		new_fd = accept(sockfd, (struct sockaddr *)&their_addr, &sin_size);
		if (new_fd == -1) {
			perror("accept");
			continue;
		}

		inet_ntop(their_addr.ss_family,
			get_in_addr((struct sockaddr *)&their_addr),
			s, sizeof s);
		printf("server: got connection from %s\n", s);

		if (send(new_fd, &handshake, sizeof(int), 0) == -1)
			perror("handshake");

		if( (size = recv(new_fd, &validate_handshake, sizeof(int), 0)) == -1){
					perror("rec");
		}

		if( validate_handshake == handshake + 1 ){
			size = strlen(s);
			printf("connection commited to %s\n", s);
			authenticate = 1;
			if (send(new_fd, &authenticate, sizeof(int), 0) == -1)
				perror("send");
			/*strcpy(buffer,s);
			if( (fd = open( buffer, O_CREAT, 0640) ) == -1 ){
				perror("error creating clients database");
			} else{
				fileCount++;
			}*/

			if (!fork()) { // this is the child process
				close(sockfd); // child doesn't need the listener
				
				//prompt
				while( strcmp(message,"exit") ){
					//printf( "Sending: passwd\n");
					message[0] = '-';
					message[1] = '>';
					message[2] = '\0';
					hideMessage(message);
					if (send(new_fd, message, 2, 0) == -1)
						perror("send");
					if( (size = recv(new_fd, message, 100, 0)) == -1)
						perror("rec");
					message[size] = '\0';
					recoverMessage(message);

					if( strcmp(message, "request") == 0 ){
						int i;
						
						if( (i = recv(new_fd, filename, 99, 0) ) == -1 ){
									perror("getting binary name");
						}
						filename[i] = '\0';
						recoverMessage(filename);
						strcpy(bin_data.name, filename);
						strcat(filename, infotag);
						//printf("Where is %s?\n",filename);
						fpp = fopen(filename,"r");

						if( fpp == NULL ){
							i = -1;
							//send client -1 to indicate something went wrong
							if( (send(new_fd, &i, sizeof(int), 0) ) == -1 ){
								perror("Failed to notify client of error.");
							}
						}else{
							i = 1;
							if( (send(new_fd, &i, sizeof(int), 0) ) == -1 ){
								perror("Failed to notify client of error.");
							}
							handshake = ( (int) &new_fd ) *100 % 2379;
							//printf("H = %d\n",handshake);
							validate_handshake = handshake;
							//wait for verification from client to send
							//printf("Validate handshake\n");
							if( (send(new_fd, &handshake, sizeof(int), 0) ) == -1 ){
								perror("could not verify handshake");
							}
							if( (recv(new_fd, &handshake, sizeof(int), 0) ) == -1 ){
								perror("could not verify handshake");
							}
							//printf("H = %d\n",handshake);
							if( handshake == (validate_handshake + 1) ){
								//ok to send data packet
								char line[MAXDATASIZE];
								char buf[5000];
								buf[0] = ' ';
								buf[1] = '\0';
								if( admin ){
									//printf("send to ADMIN\n");
									
									while(fgets(line,100,fpp)){
										strcat(buf,line);
									}
									if( (send(new_fd, buf, strlen(buf), 0) ) == -1 ){
										perror("could not verify handshake");
									}

								}else{
									i = 0;
									while(fgets(line,100,fpp) && i < 4){
										strcat(buf,line);
										i++;
									}
									if( (send(new_fd, buf, strlen(buf), 0) ) == -1 ){
										perror("could not verify handshake");
									}
								}
							
							}else{
								//not ok to send
								perror("Data packet handshake failed.");
								if( send(new_fd, buffer, size, 0) == -1 )
									perror("Failed sending packet to client.");
							}
						}
					}

					if( strcmp(message, "admin") == 0 ){
						//wait for verification from client to send
						if( (recv(new_fd, &verifyAdmin, sizeof(unsigned short), 0) ) == -1 ){
							perror("could not verify admin");
						}

						if(verifyAdmin){
							admin = 1;
							message[size] = '1';
						}else{
							message[size] = '0';
							admin = 0;
						}

						message[size+1] = '\0';
					}
					if( strcmp(message, "verify") == 0 ){
						int verifySend = 0;
						//wait for verification from client to send
						if( (size = recv(new_fd, &verifySend, sizeof(unsigned int), 0) ) == -1 ){
							perror("could not verify send");
						}

						if( verifySend == 1 ){
							//printf("NOMNOMNOM\n");
							if( (size = recv(new_fd, &bin_data, sizeof(ELF_data), 0) ) == -1 ){
								perror("rec");
							}
							decDATA(&bin_data);

							if( checkSum(&bin_data) ){
								//file writing
								strcpy(fname,bin_data.name);
								strcat(fname, infotag);
								if( (fp = fopen(fname,"a")) == NULL ){
									perror("Failed to open client database.");
								}
								//fclose(fp);
								fprintf(fp,"\t%s\n", bin_data.name);
								fprintf(fp,"\tABI: %d\n", bin_data.ABI);
								fprintf(fp,"\t.text: (bytes)%d \n", bin_data.text);
								fprintf(fp,"\tentry: 0x%x\n", bin_data.entry);
								fprintf(fp,"\t.data: (bytes)%d\n", bin_data.data_sz);
								fprintf(fp,"\t.bss: %d (bytes)\n", bin_data.bss_sz);
								fprintf(fp,"\tchecksum: 0x%x\n", bin_data.checksum);
							}else{
								printf("File not saved. Checksum failed!");
							}
						}
					}

					printf( "Client: %s --> ",s );
					//printf( "%s\n",message );
					//fflush(stdout);
				}

				//exit prompt
				//printf( "Sending: exit\n");
				message[0] = 'e';
				message[1] = 'x';
				message[2] = 'i';
				message[3] = 't';
				message[4] = '\0';
				hideMessage(message);
				if (send(new_fd, message, 5, 0) == -1)
					perror("send");

				//we're done with this client
				printf("server: connection lost \n");
				
				close(new_fd);
				exit(0);
			}
		
		}else{
			authenticate = 0;
			printf( "server: connection rejected \n");
			if (send(new_fd, &authenticate, sizeof(int), 0) == -1)
				perror("send");
		}
		close(new_fd);  // parent doesn't need this		
	}

	return 0;
}
