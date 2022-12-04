# Gneiss: Capstone Packet Management Web Application

## Abstract:
The Boise State University Mechanical and Biomedical Engineering (MBE) department seeks a means of integrating multiple aspects of their Senior Capstone Project lab equipment purchasing and invoicing process, to expedite repetitive tasks and streamline procurement. MBE uses a wide variety of documenting and logistical procedures in supporting the design of these capstone projects, consisting of numerous purchase invoices, spreadsheets, documents, and data from 3rd party applications. 

Much of this process is currently managed by hand, a significant effort on the MBE department and its staff. Our project, 'Paketto', allows users to integrate many of these diverse sources of information into digital packets stored within a single web application. Paketto then organizes these packets within a database for easy access to purchasing information and invoices. This database is designed in a manner where it can be accessed in a secure manner through user accounts created through the application and administrated by the MBE department.

The aim of the project is to automate the process of storing and fetching contract information, create a more uniform means of organizing and presenting that information, and reducing the logistical burden involved in the procurement process, allowing for reduced effort on the part of the MBE department and its resources.

## Members:
 - Connor Lawrence
 - Humzza Hargan
 - Jackson Theel
 - Jose Aguilar
 - Phillip Newell
 - Teddy Ramey

## Project Description:
Paketto consists of two back-end databases, an SQL database for handling user accounts & permissions, and a no-SQL Redis database for the storage of packets. The no-SQL format allows for the Redis database to be adjusted by the end user, should the department want to restructure its data formatting in the future. The databases are integrated into a React web application, consisting of account login and creation pages, a main page for managing a user's packets and creating new ones, as well as admin pages for managing the app's user permissions.

## Pictures
![Account Creation](https://github.com/cs481-ekh/f22-gneiss/blob/main/docs/Account%20Creation.png)
![Main Page](https://github.com/cs481-ekh/f22-gneiss/blob/main/docs/Main%20Page.png)
![Approval Step](https://github.com/cs481-ekh/f22-gneiss/blob/main/docs/Approval%20Step.png)
![Admin Page](https://github.com/cs481-ekh/f22-gneiss/blob/main/docs/adminGetBanned.png)
