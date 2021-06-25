# ReportSmasher
A java desktop application to complete my daily report at work, so I don't have to do anything xD

## How to use the app
* Gather all the report files
* Add the system export file to first the option
* Report all lines for the second option
* Report file for the third option
* Select the destination report to update
* Select a date to update
* Click on Start to begin

## Project tools
This project used Maven build tools and also included other libraries such as
* Apache Commons CSV: to read and write CSV files
* Apache Poi: read and write .xls and .xlsx files in Microsoft Excel
* JCalendar: provide a simple GUI for clients to choose a date

## Side notes
* The project doesn't have Unit tests mainly because I haven't learn JUnit
* It's my first Java project, so I tried my best to organize all classes and source files
* Testing were done manually before finallization
* This project was built so that everyone can use it, therefore, GUI component was added

## IDLE
* This project used JDK 11
* Eclipse 2019-12

** You might need to change the build path first before running the project. Replace my username, with your computer's username **
## How to change build path
* Right click on the project
* Choose build path -> Configure build path
* In the Library tab, click on poi-ooxml-4.1.1.jar
* Choose Edit and locate the poi-ooxml-4.1.1.jar library from your computer username
