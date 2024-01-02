 
CREATE TABLE IF NOT EXISTS Printers (
    PrinterID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255)  NOT NULL UNIQUE ,    
   Location VARCHAR (100),
   Status VARCHAR (20),
    Description TEXT,
    IsActive BOOLEAN
);

CREATE TABLE IF NOT EXISTS `tb_utilisateur`
(
   `id` int (11) NOT NULL AUTO_INCREMENT,
   `nom` varchar (250) NOT NULL,
   `prenom` varchar (250) NOT NULL,
   `naissance` date NOT NULL,
   `username` varchar (250) NOT NULL,
   `password` varchar (250) DEFAULT NULL,
   `fonction` varchar (250) DEFAULT NULL,
   `description` varchar (250) DEFAULT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `username` (`username`)
)
ENGINE= InnoDB DEFAULT CHARSET= latin1 AUTO_INCREMENT= 1;
INSERT INTO `tb_utilisateur`
(
   `id`,
   `nom`,
   `prenom`,
   `naissance`,
   `username`,
   `password`,
   `fonction`,
   `description`
)
VALUES
(
   1,
   'MADANI',
   'Hamid',
   '1972-10-03',
   'drmdh@msn.com',
   'azerty@26',
   'Ophtalmologue',
   'PoXviCZNZBHGyTsac6XdKQ==:lTbS2WuC5p/tMyEVhLPfNA=='
);


 
CREATE TABLE IF NOT EXISTS   Roles (
    RoleID INT AUTO_INCREMENT PRIMARY KEY,
    RoleName VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS   Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL, -- Consider security best practices for storing passwords
    RoleID INT,
    FOREIGN KEY (RoleID) REFERENCES Roles(RoleID)
);

CREATE TABLE IF NOT EXISTS   Games (
    GameID INT AUTO_INCREMENT PRIMARY KEY,
    GameName VARCHAR(255) NOT NULL UNIQUE,
    AgeRestriction ENUM('minor', 'teen', 'adult') NOT NULL
);

CREATE TABLE IF NOT EXISTS   Permissions (
    PermissionID INT AUTO_INCREMENT PRIMARY KEY,
    ActionText VARCHAR(255) NOT NULL UNIQUE 
);

 
CREATE TABLE IF NOT EXISTS   UserRoles (
    UserID INT,
    RoleID INT,
    PRIMARY KEY (UserID, RoleID),
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (RoleID) REFERENCES Roles(RoleID)
);



 
 

CREATE TABLE IF NOT EXISTS RolePermissions (
    RoleID INT,
    PermissionID INT,
    PRIMARY KEY (RoleID, PermissionID),
    FOREIGN KEY (RoleID) REFERENCES Roles(RoleID) ON DELETE CASCADE,
    FOREIGN KEY (PermissionID) REFERENCES Permissions(PermissionID) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS GamePrinter (
    GameID INT,
    PrinterID INT,
    PRIMARY KEY (GameID, PrinterID),
    FOREIGN KEY (GameID) REFERENCES Games(GameID),
    FOREIGN KEY (PrinterID) REFERENCES Printers(PrinterID)
);


