# Kafka Bridge CLI

## Introduction

The **Kafka Bridge CLI** is a commandline interface (CLI) tool that allows you to interact with a Kafka HTTP bridge it's APIs.

## Build

To build the kafka-bridge CLI run

	quarkus build --no-tests

You can also create a native executable like this

	quarkus build --no-tests --native

## Configure
To configure the Kafka Brige CLI create a **config/application.properties** and define **kafka.bridge.url** like this

## Run
The Kafka Bridge CLI can be used to launch a single command, run multiple commands in batch mode or also in an interactive mode.

For convenience you can also set an alias like this

	alias kafka-bridge="java -jar target/quarkus-app/quarkus-run.jar"

### Standard mode
To launch the Kafka Bridge CLI execute

	java -jar target/quarkus-app/quarkus-run.jar

You should now see the following options:

	Usage:  [-hV] COMMAND
  	-h, --help      Show this help message and exit.
  	-V, --version   Print version information and exit.
	Commands:
  	  info
  	  health
  	  openapi
  	  ready
  	  consumers
  	  topics
### Batch mode
To run the Kafka Bridge CLI in batch mode add run

 	java -jar target/quarkus-app/quarkus-run.jar -f <batch-file>

In the batch files you can also assign results to variables and apply filters, both may of course be combined. You can also comment out lines by using **#** as the first character of the line.
### Interactive mode
To run Kafka Bridge CLI in interactive/shell mode run

	java -jar target/quarkus-app/quarkus-run.jar -i

You should now see a prompt like this

	kafka-bridge>

Just hit **<enter>** for a list of available commands.

Type **exit** to exit the shell.

#### Variables
In the batch file or in the shell you can also assign the outcome of a command to a variable like this

	assign variable <VARIABLE_NAME>=<COMMAND>

To reference a variable in the batch file you specify

	${VARIABLE_NAME}

In case the variable is not assigned in the batch file itself, the Kafka Brigge CLI will look for an environment variable with the same name.

To show the value of a variable you can use the **echo** command like this

	echo ${VARIABLE_NAME}

#### Filters
You can also apply filters to a result of a command like this

	<COMMAND>|<FILTER>

Currently supported filters are **xpath**, **jsonpath**, **prettyprint** and **json2xml**.

You can also specify more than one filter

	<COMMAND>|<FILTER1>|<FILTER2>

for example for converting json to xml and then applying xpath.
##### XPath
To apply an xpath filter specify the filter like this

	<COMMAND>|xpath <XPATH_EXPRESSION>
##### Jsonpath
To apply an jsonpath filter specify the filter like this

	<COMMAND>|jsonpath <JSONPATH_EXPRESSION>
##### Prettyprint
For pretty printing the output run

	<COMMAND>|prettyprint
### Variables and Filters
For combining variables and filters use the following syntax

	assign variable <VARIABLE_NAME>=<COMMAND>|<FILTER1>|<FILTER2>
#### Recording
You can record individual commands into a text file, for example for later batch processing

To start recording execute
```
recording start <textfile>
```
Afterwards on each command you can confirm wheter or not it should be added to the recording

To end the recording enter
```
recording stop
```
### Example Commands
```bash
topics create pet src/test/topics/create-topic.json
topics create pets src/test/topics/create-topic.json

topics create pet src/test/topics/records-pet.json

consumers create pet-consumer src/test/consumer/create-consumer.json
consumers assignments pet-consumer pet-consumer src/test/consumer/create-assignments.json
consumers offsets pet-consumer pet-consumer src/test/consumer/create-offsets.json
consumers positions create pet-consumer pet-consumer src/test/consumer/positions/create-positions.json
consumers positions beginning pet-consumer pet-consumer src/test/consumer/positions/offsets-beginning.json
consumers positions end pet-consumer pet-consumer src/test/consumer/positions/offsets-end.json
consumers subscriptions create pet-consumer pet-consumer src/test/consumer/create-subscription.json
consumers subscriptions topics pet-consumer pet-consumer

consumers records pet-consumer pet-consumer

consumers suscriptions delete pet-consumer pet-consumer
consumers delete pet-consumer pet-consumer
```