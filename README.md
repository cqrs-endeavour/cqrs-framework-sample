# cqrs-framework-sample

## Overview
This project showacses a very simple application built with abstractions available in cqrs-framework that was created during work on 
master thesis on AGH UST in cooperation with [Lufthansa Systems GmbH & Co. KG](http://www.lhsystems.com/) and as part of EU PaaSage FP7 project (grant 317715).

## Requirements
The framework is based on [akka-persistence](http://doc.akka.io/docs/akka/2.4.0/scala/persistence.html) which has pluggable storage backends
but for now cqrs-framework only supports [Cassandra](http://cassandra.apache.org/) as journal and snapshot storage and [Kafka](http://kafka.apache.org/)
as event bus solution. Because of that to run the examples one has to have both of those components installed on the system.

Here is a list of port configuration that is required by default (of course each of this settings can be modified in *application.conf*)

* zookeeper - 127.0.0.1:2181
* kafka broker - 127.0.0.1:9092
* cassandra - 127.0.0.1:9042

those should be ports that are configured by default installations of mentioned software.

## Usage

There are two main applications that are available:

* WriteModelMain
* ReadModelMain

each one of them can be executed through SBT.

```scala
> run
[warn] Multiple main classes detected.  Run 'show discoveredMainClasses' to see the list

Multiple main classes detected, select one to run:

 [1] WriteModelMain
 [2] ReadModelMain

Enter number: 
```

### WriteModelMain
This application is responsible for starting single write model node and accepts single run argument: configuration file suffix, e.g. 
if you invoke ```run 1``` in *sbt* and select *WriteModelMain* the configuration will be taken from *write-model-1.conf* file. 

Default configuration required at least two nodes to form a cluster so you have to invoke *WriteModelMain* with ```run 1``` and ```run 2```.
The Aggregate Roots will be shared between nodes thanks to *akka-cluster-sharding*.

When everything is initialized properly there will be text like 

```
available commands:
create
<accountId>,<delta>
```

displayed in the console. Those are available commands. If ```create``` is typed new account will be created and message like 
```+++Created account 7cf44d64-32ab-4ea2-a087-6c87e8e67c6b+++``` should be visible in the logs. Using this identifier it is possible to 
change account balance by typing ```7cf44d64-32ab-4ea2-a087-6c87e8e67c6b,5``` (this command changes balance of account by 5). You should look then
for message like ```+++Changed balance of account 7cf44d64-32ab-4ea2-a087-6c87e8e67c6b by 5, now has 5+++```. By typing negative number it is possible
to decrease account balance. 

### ReadModelMain
There is no hard limit on number of running read models. Each read model reconstructs state based on events in Cassandra and Kafka. 
This read model also implements duplication filtering using *InMemoryFilter* and stores all its data in memory.

Read model supports three different queries
```
1 - query bank money
2 - query number of clients
3 - list of all accounts
```

To issue one of them just type number and hit enter.
