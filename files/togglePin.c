#include <stdio.h>
#include <stdlib.h>
#include <gpiod.h>
#include <unistd.h>
#include <string.h>

#define CONSUMER "Ice Station Zebra"
int pin = 16;
int trigger = 16;

// argv[0] = uri
// argv[1] = pin number
// argv[2] = pin value

int main(int argc, char **argv){
	int pin_arg = 1;
	int value_arg = 1;
	if(argc < 3){
		printf("%s <PIN NUMBER><PIN STATE>",argv[0]);
		return -1;
	}
	pin = atoi(argv[1]);
	int state = atoi(argv[2]);


	printf("You entered: %d\n",state);

	
	struct gpiod_chip *chip;
	struct gpiod_line *line;
	struct gpiod_line *trigger_line;
	chip = gpiod_chip_open_by_name("gpiochip0");
	
	if(!chip){
		perror("Chip is null");
		return -2;
	}
	
	// pin
	line = gpiod_chip_get_line(chip, pin);
	if(!line){
		perror("Line is null");
		return -3;
	} 
	
	int ret = gpiod_line_request_output(line, CONSUMER,0);
	if(ret < 0){
		perror("Request line as output failed");
		gpiod_line_release(line);
		gpiod_chip_close(chip);
		return -4;
	}
	gpiod_line_set_value(line, state);
	printf("Pin %d is set to %d\n",pin, state);
	gpiod_line_release(line);
	gpiod_chip_close(chip);
	return 0;	
	
}
