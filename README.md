# MySQLRemoteGUI

A two-tier Java based client-server application interacting with a MySQL 
database utilizing JDBC for the connectivity. This project was designed to gain you some experience 
using the various features of JDBC and its interaction with a MySQL DB Server environment.

## How it Works
GUI allows client or root user to execute SQL commands against an SQL server that the client selects. The user selects the JDBC driver to use and the database URL which corresponds to the database on which commands should be executed. The GUI has UI elements for the user to connect to the database, enter commands, execute commands, and see the results.

### Simple SELECT
![Command #2C](https://github.com/KShervington/MySQLRemoteGUI/assets/54691558/3b4ec794-66d5-4817-8d0f-ef1fa4975c85)

### Complex SELECT
![Command #1](https://github.com/KShervington/MySQLRemoteGUI/assets/54691558/c217002a-bd70-4c2e-8a13-3a0e3c32c002)

### Client user prevented from making database changes
![Command #2B](https://github.com/KShervington/MySQLRemoteGUI/assets/54691558/b084525b-719c-45b2-8c1c-609cc277d80f)

### Root User Has Elevated Permissions
| Root User UPDATE | UPDATE Result |
|------------------|---------------|
| ![Command #8B](https://github.com/KShervington/MySQLRemoteGUI/assets/54691558/0dca1574-cd6d-472e-92ac-e4a764075eb1)         | ![Command #8C](https://github.com/KShervington/MySQLRemoteGUI/assets/54691558/400ba3fe-cb04-4f99-aa3c-940fe71fadb5)      |
