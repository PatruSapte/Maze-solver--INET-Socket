/**** iserver.c ****/

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <netinet/in.h>
#include <unistd.h>

#define SERVER_PORT 12345
#define BUFSIZE 1//higher values can result in sending file incompletely

#define SOLVED_IMAGE "SolvedImage.png"
#define SOLVED_ASCII "SolvedMaze.tek"
/* alert colors */

#define RED   "\x1B[31m"
#define GRN   "\x1B[32m"
#define YEL   "\x1B[33m"
#define BLU   "\x1B[34m"
#define RESET "\x1B[0m"

 
 
bool file_exists (char *filename);

void alert(char* msg, char* color);

char sys_call[5000];

int main(int argc, char *argv[])
{

	int i, len, num, rc;
	int listen_sd, accept_sd;

	struct sockaddr_in addr;

	/* Create an AF_INET stream socket to receive */
	/* incoming connections on */

	listen_sd = socket(AF_INET, SOCK_STREAM, 0);

	if(listen_sd < 0)
	{
		perror("Iserver - socket() error");
		exit(-1);
	} 
	else printf("Iserver - socket() is OK\n");	 

	/* Bind the socket */
	
	printf("Binding the socket...\n");

	memset(&addr, 0, sizeof(addr));
	addr.sin_family = AF_INET;
	addr.sin_addr.s_addr = htonl(INADDR_ANY);
	addr.sin_port = htons(SERVER_PORT);

	rc = bind(listen_sd, (struct sockaddr *)&addr, sizeof(addr));
	if(rc < 0)
	{
		perror("Iserver - bind() error");
		close(listen_sd);
		exit(-1);
	}
	else printf("Iserver - bind() is OK\n"); 

	
	/* Set the listen backlog */
	rc = listen(listen_sd, 5);
	if(rc < 0)
	{
		perror("Iserver - listen() error");
		close(listen_sd);
		exit(-1);
	}
	else printf("Iserver - listen() is OK\n");


	/* Inform the user that the server is ready */
	alert("The Iserver is ready!\n", GRN);


	for(;;)
	{

	//   Wait for an incoming connection 

		printf(" waiting on accept()\n");
		accept_sd = accept(listen_sd, NULL, NULL);

		if(accept_sd < 0)
		{
			perror("Iserver - accept() error");
			close(listen_sd);
			exit(-1);
		}
		else printf("accept() is OK and completed successfully!\n");

		printf("I am waiting client(s) to send file data to me...\n");


		//waiting for client to send file name 
		char file_name[256]="";    
		read(accept_sd, file_name, sizeof(file_name));	

		//Read file byte array and copy in files
		char file_array[BUFSIZE];
		char file_path[300] = "server_files/";
	
		strcat(file_path, file_name);
		FILE *the_file = fopen(file_path, "w");

		printf("Client is sending me %s\n",file_name);

		int nb = read(accept_sd, file_array, BUFSIZE);
		while(nb>0) {
			fwrite(file_array, 1, nb, the_file);
			nb = read(accept_sd, file_array, BUFSIZE);	
		}

		
		fclose(the_file);
		printf("File recieved\n");
		
		//call java solver 
		sprintf(sys_call, "java Main.java %s", file_path);		
	    system(sys_call);
		
		//send resulted file to client

		char *solved_path;
		if(file_exists(SOLVED_IMAGE))
		{
			solved_path = SOLVED_IMAGE;
		}
		else
		{
			solved_path = SOLVED_ASCII;
		}
		
		
		FILE *solved_file = fopen(solved_path, "r");
		printf("Sending back %s", solved_path);
		
		write(accept_sd, solved_path, strlen(solved_path)+1);
		printf("to client\n");
		
		char send_buffer[BUFSIZE];
		nb = fread(send_buffer, 1, sizeof(send_buffer), solved_file);
		
		printf("to client\n");
		while(!feof(solved_file)) {
			write(accept_sd, send_buffer, nb);
			nb = fread(send_buffer, 1, sizeof(send_buffer), solved_file);

		}
		
		
		/*//  Receive a message from the client 
		
		printf("I am waiting client(s) to send message(s) to me...\n");

		rc = recv(accept_sd, buffer, sizeof(buffer), 0);

		if(rc <= 0)

		{

		perror("Iserver - recv() error");

		close(listen_sd);

		close(accept_sd);

		exit(-1);

		}

		else

		printf("The message from client: \"%s\"\n", buffer);

		//  Echo the data back to the client 

		printf("Echoing it back to client...\n");

		len = rc;

		rc = send(accept_sd, buffer, len, 0);

		if(rc <= 0)

		{

		perror("Iserver - send() error");

		close(listen_sd);

		close(accept_sd);

		exit(-1);

		}

		else

		printf("Iserver - send() is OK.\n");

		//   Close the incoming connection 
		*/
		close(accept_sd);

	}

	//    Close the listen socket 

	close(listen_sd);

	return 0;

}
bool file_exists (char *filename) {
	struct stat   buffer;   
  	return (stat (filename, &buffer) == 0);
}
void alert(char* msg, char* color)
{
	printf("\%s%s" RESET,color, msg);  

}
