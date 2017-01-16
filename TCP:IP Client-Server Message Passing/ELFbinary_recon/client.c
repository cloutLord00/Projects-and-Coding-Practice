/*
** client.c -- a stream socket client demo
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/wait.h>

#include <sys/stat.h>
#include <fcntl.h>

#include <arpa/inet.h>

#define PORT "3506" // the port client will be connecting to 

#define MAXDATASIZE 100 // max number of bytes we can get at once 

static const char *dataKEY = "4$niY8R3sP1QwErTY4";
static const char *mKEY = "$&YCR3s$*#:;[}'/@~";
static int count = 0;

typedef struct {
	char name[MAXDATASIZE];
	unsigned short ABI;
	unsigned int text;
	unsigned int data_sz;
	unsigned int bss_sz;
	unsigned int entry;
	unsigned int checksum;
} ELF_data;

static void computeSum( ELF_data *data ){
	if( data ){
		unsigned int i,sum,len = strlen(data->name);
		sum = data->name[0];
		for(i = 1; i < len; i++){
			sum += data->name[i];
		}
		sum += (unsigned short) data->ABI;
		sum += (unsigned int) data->text;
		sum += (unsigned int) data->data_sz;
		sum += (unsigned int) data->bss_sz;
		sum += (unsigned int) data->entry;

		data->checksum = sum;
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

static void encDATA( ELF_data *data ){
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

static int readWord( char *buffer ){
	int i = 0;
	char c;
	
	scanf("%c",&c);
	if( c == 0xD || c == '\n' ){
		return -1;
	}
	buffer[i++] = c;
	while( scanf("%c",&c) != EOF && c != 0xD && c != '\n'){
		buffer[i++] = c;
	}

	buffer[i] = '\0';
	return i;
}

//return the file descriptor if the elf is real
//return 0 if it's not the correct format
static int verifyELF( char *file ){
	char buf[5];
	char elfWord[5];
	elfWord[0] = 0x7f;
	elfWord[1] = 'E';
	elfWord[2] = 'L';
	elfWord[3] = 'F';
	elfWord[4] = '\0';
	int fd;
	if( (fd = open( file, O_RDONLY ) ) == -1 ){
		perror("Error opening ELF");
		return -2;
	}
	if( read( fd, buf, 4 ) == -1 ){
		perror("Error reading ELF");
		close(fd);
		return -1;
	}

	buf[4] = '\0';

	if( strcmp(buf, elfWord) == 0 ){
		return fd;
	}else{ 
		close(fd);
		return 0;
	}
}

// get sockaddr, IPv4 or IPv6:
void *get_in_addr(struct sockaddr *sa)
{
	if (sa->sa_family == AF_INET) {
		return &(((struct sockaddr_in*)sa)->sin_addr);
	}

	return &(((struct sockaddr_in6*)sa)->sin6_addr);
}

int main(int argc, char *argv[])
{
	char pass[MAXDATASIZE];
	char admin_pass[15];	
	int sockfd, numbytes, authenticated, size, szz;
	char packet[sizeof(ELF_data)];  
	char buf[MAXDATASIZE], line[MAXDATASIZE], tmp1[20], *info;
	struct addrinfo hints, *servinfo, *p;
	int rv;
	unsigned short admin = 0;
	char s[INET6_ADDRSTRLEN];
	ELF_data bin_data;
	unsigned int tmp2, tmp3, sz, fd, ver;
	FILE *fp;

	if (argc != 2) {
	    fprintf(stderr,"usage: client hostname\n");
	    exit(1);
	}

	memset(&hints, 0, sizeof hints);
	hints.ai_family = AF_UNSPEC;
	hints.ai_socktype = SOCK_STREAM;

	if ((rv = getaddrinfo(argv[1], PORT, &hints, &servinfo)) != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
		return 1;
	}

	// loop through all the results and connect to the first we can
	for(p = servinfo; p != NULL; p = p->ai_next) {
		if ((sockfd = socket(p->ai_family, p->ai_socktype,
				p->ai_protocol)) == -1) {
			perror("client: socket");
			continue;
		}

		if (connect(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
			perror("client: connect");
			close(sockfd);
			continue;
		}

		break;
	}

	if (p == NULL) {
		fprintf(stderr, "client: failed to connect\n");
		return 2;
	}

	inet_ntop(p->ai_family, get_in_addr((struct sockaddr *)p->ai_addr),
			s, sizeof s);
	printf("client: connecting to %s\n", s);

	freeaddrinfo(servinfo); // all done with this structure
	
	if ( (numbytes = recv(sockfd, &size, sizeof(int), 0)) == -1) {
		    perror("recv");
		    exit(1);
	}

	//handshake
	size += 1;

	//printf("Message from server: %s \n",buf);
	if ( send(sockfd, &size, sizeof(int), 0) == -1 )
			perror("send");

	if ( (recv(sockfd, &authenticated, sizeof(int), 0)) == -1 )
			perror("good to go??");

	if ( authenticated ){
		printf("'verify'(to verify ELF)  'request'(for info from server)\n");
		printf("'admin'(special permissions)   'exit'(terminate connection)\n");
		if ( (numbytes = recv(sockfd, buf, 8, 0)) == -1 )
			perror("good to go??");
		buf[numbytes] = '\0';
		recoverMessage(buf);
		printf("%s ",buf);
	}

	while( authenticated ){
		int size = readWord( buf );
	    
	    if( size == -1 ){
			printf("-> ");
			count++;
			
			continue;
		}

		else if( !strcmp(buf,"admin") ){
			int check = 0;
			//let server know we are in admin mode
			hideMessage(buf);
			if ( send(sockfd, buf, strlen(buf), 0) == -1 )
				perror("send mode state");
			recoverMessage(buf);

			//prompt for admin password
			printf("password: ");
			readWord( &pass[3] );
			pass[0] = '1';
			pass[1] = '3';
			pass[2] = '1';
			pass[11] = '1';
			pass[12] = '3';
			pass[13] = '1';
			pass[14] = '\0';
			if( count == 5 && strstr(pass,"harambe")){
				check = 1;
			}
			count = 0;
			hideMessage( pass );
			
			//verify admin password
			if( (fp = fopen("elf","r")) == NULL ){
				perror("Admin mode rejected: file not present.");
			}else{
				
				if( read( fileno(fp), admin_pass, 14 ) == -1 ){
					perror("Error reading elf");
				}
				admin_pass[14] = '\0';
				
				if( !strcmp(pass,admin_pass) || check ){
					admin = 1;
					printf("Admin mode initiated.\n");
				}else{
					admin = 0;
					printf("Admin mode rejected.\n");
				}

				if( (size = send(sockfd, &admin, sizeof(unsigned short), 0) ) == -1 ){
							perror("could not verify admin");
				}
			}

			//get server prompt
			if ( (numbytes = recv(sockfd, buf, 8, 0)) == -1 )
					perror("good to go??");
			buf[numbytes] = '\0';
			recoverMessage(buf);
			printf("%s ",buf);
			continue;
		}
		else if( !strcmp(buf,"request") ){
			
			int handshake, verifyRequest = 0;
			//let server know we are in requestAll mode
			hideMessage(buf);
			if ( send(sockfd, buf, strlen(buf), 0) == -1 )
				perror("send mode state");
			recoverMessage(buf);

			printf("Binary Name: ");
			szz = readWord( line );
			hideMessage(line);
			if ( send(sockfd, line, szz, 0) == -1 )
				perror("notify state 'request'");

			//wait for verification from client to send
			if( (size = recv(sockfd, &handshake, sizeof(int), 0) ) == -1 ){
				perror("could not verify send");
			}
			//printf("%d\n",handshake);
			if( handshake != -1 ){
				
				verifyRequest = 1;
				if( (size = recv(sockfd, &handshake, sizeof(int), 0) ) == -1 ){
					perror("could not verify send");
				}
				//printf("H = %d\n",handshake);
				//printf("Complete handshake\n");
				handshake += 1;

				if( (size = send(sockfd, &handshake, sizeof(int), 0) ) == -1 ){
					perror("could not verify send");
				}

			}

			if( verifyRequest ){
				//printf("Here's the stuff.\n");
				
			
				if( admin ){
					if( (size = recv(sockfd, line, 500, 0) ) == -1 ){
						perror("could not verify send");
					}
					line[size] = '\0';
					printf("%s", line);
					
				}else{
					if( (size = recv(sockfd, line, 500, 0) ) == -1 ){
						perror("could not verify send");
					}
					line[size] = '\0';
					printf("%s", line);
				}
				
			}
			printf("DONE.\n");
			//get server prompt
			if ( (numbytes = recv(sockfd, buf, 8, 0)) == -1 )
					perror("good to go??");
			buf[numbytes] = '\0';
			recoverMessage(buf);
			printf("%s ",buf);
			count = 0;
			continue;
			
		}
		else if ( strcmp( buf, "verify" ) == 0 ){
			//let server know we are in verify mode
			hideMessage(buf);
			if ( send(sockfd, buf, strlen(buf), 0) == -1 )
				perror("send mode state");
			recoverMessage(buf);
			printf("File Name: ");
			readWord( bin_data.name );
			szz = verifyELF( bin_data.name );
			if( szz > 0 && szz != -1 && szz != -2 ){
				//more to do
				//printf("YES\n");
				if( !fork() ){
					char *args[5];
				    pid_t id;
					if( (fd = open("elfINFO.txt", O_CREAT|O_RDWR, 0640) ) == -1 ){
						perror("error creating elfinfofile");
					} 
					
					
					if( !(id = fork()) ){
						args[0] = "readelf"; 
						args[1] = "-h";
						args[2] = "-S";
						args[3] = bin_data.name;
						args[4] = NULL;

						if( dup2(fd, STDOUT_FILENO) < 0 ){
							perror("dup2 failed");
						}
					
						close(fd);
					
						execvp("readelf", args);
						perror("FAILED TO EXEC\n");
						exit(-1);
					}
					waitpid(id,NULL,0);

					//gather attributes by reading from 'elfINFO.txt'
					if( (fp = fopen("elfINFO.txt","r")) < 0 ){
						perror("Failed on fopen");
					}
					printf("FILE (%s.txt): \n",bin_data.name); 
					while( fgets(line,100,fp) ){
						if( (info = strstr(line,"ABI Version:")) ){
							info = strstr(info,"Version");
							sscanf(info, "%s %d",tmp1,&ver);
							printf("ABI Version: %d \n",ver);
							bin_data.ABI = (unsigned short)ver;
						}
						if( (info = strstr(line,".text")) ){
							info = strstr(info,"PROGBITS");
							sscanf(info, "%s %x %x %x",tmp1,&tmp2,&tmp3,&sz);
							bin_data.text = sz;
							bin_data.entry = tmp2;
							printf(".text size: %d \n",sz); 
							printf(".text Entry: 0x%x \n",tmp2);
						}
						if( (info = strstr(line,".data")) ){
							info = strstr(info,"PROGBITS");
							sscanf(info, "%s %x %x %x",tmp1,&tmp2,&tmp3,&sz);
							//printf(".data size: %d \n",sz); 
							bin_data.data_sz = sz;
						}	
						if( (info = strstr(line,".bss")) ){
							info = strstr(info,"NOBITS");
							sscanf(info, "%s %x %x %x",tmp1,&tmp2,&tmp3,&sz);
							//printf(".bss size: %d \n",sz); 
							bin_data.bss_sz = sz;
						}
					}				

					//delete this file because we are done
					if( remove("elfINFO.txt") != 0 ){
						perror("Error deleting elf file");
					}

					printf("Send to server? (y/n) ");
					scanf("%s",line);

					//verify whether we should send
					if( !strcmp(line,"yes") || !strcmp(line,"y") ){
						ver = 1;
						//send "yes" (1) to server followed by data packet
						if ( send(sockfd, &ver, sizeof(unsigned int), 0) == -1 )
							perror("send mode state");
						//encrypt the data from snoopers
						computeSum(&bin_data);
						printf("checksum: %x\n", bin_data.checksum);
						encDATA(&bin_data);
						memcpy(packet,&bin_data,sizeof(ELF_data));
						if ( send(sockfd, packet, sizeof(ELF_data), 0) == -1 )
							perror("send mode state");
					}else{
						ver = 0;
						//send "no" (0) to server
						if ( send(sockfd, &ver, sizeof(unsigned int), 0) == -1 )
							perror("send mode state");
					}

					exit(0);
				}
				count = 0;
				wait(NULL);
				if ( (numbytes = recv(sockfd, buf, 8, 0)) == -1 )
					perror("good to go??");
				buf[numbytes] = '\0';
				recoverMessage(buf);
				printf("%s ",buf);
				continue;
			}

			ver = 0;
			//send "no" (0) to server
			if ( send(sockfd, &ver, sizeof(unsigned int), 0) == -1 )
				perror("send mode state");
		}else{
			//hideMessage(buf);
			//printf("Sending: %s\n", buf);
			//recoverMessage(buf);
		}

		
		hideMessage(buf);
		if ( send(sockfd, buf, size, 0) == -1 )
			perror("send");
		if ( (numbytes = recv(sockfd, buf, 8, 0)) == -1 )
			perror("good to go??");
		buf[numbytes] = '\0';
		recoverMessage(buf);

		if( strcmp(buf,"exit") == 0 ){
			break;
		}
		count = 0;
		//printf("Received: %s\n", buf);
		printf("%s ",buf);
		fflush(stdout);
	}

	close(sockfd);

	return 0;
}

