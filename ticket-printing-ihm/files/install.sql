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


 
 CREATE TABLE IF NOT EXISTS   Games (
    GameID INT AUTO_INCREMENT PRIMARY KEY,
    GameName VARCHAR(255) NOT NULL UNIQUE,
      GameImage MEDIUMBLOB, 
    LogoImage MEDIUMBLOB,
    AgeRestriction ENUM('minor', 'teen', 'adult') NOT NULL
);

-- ALTER TABLE Games
-- ADD GameImage MEDIUMBLOB,
-- ADD LogoImage MEDIUMBLOB;
 
 
 
CREATE TABLE IF NOT EXISTS Printers (
    PrinterID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255)  NOT NULL UNIQUE ,    
   Location VARCHAR (100),
   Status VARCHAR (20),
    Description TEXT,
    IsActive BOOLEAN
);

CREATE TABLE IF NOT EXISTS GamePrinters (
    GameID INT UNIQUE,
    PrinterID INT,
    Name VARCHAR(255) NOT NULL,
    GameImage MEDIUMBLOB,
    PRIMARY KEY (GameID),
    FOREIGN KEY (GameID) REFERENCES Games(GameID),
    FOREIGN KEY (PrinterID) REFERENCES Printers(PrinterID)
);
CREATE TABLE GamePricing (
    PricingID INT AUTO_INCREMENT PRIMARY KEY,
    GameID INT,
    Price DECIMAL(10, 2),
    ValidFrom DATETIME,
    ValidTo DATETIME,
    FOREIGN KEY (GameID) REFERENCES Games(GameID)
);
  CREATE TABLE IF NOT EXISTS Tickets (
    TicketID INT AUTO_INCREMENT PRIMARY KEY,
    GameID INT,
    GamePricingID INT, 
    Barcode VARCHAR(255), 
    FOREIGN KEY (GameID) REFERENCES Games(GameID) ,
    FOREIGN KEY (GamePricingID) REFERENCES GamePricing(PricingID) -- Adjust according to your actual FK constraints
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

LOCK TABLES `printers` WRITE;
INSERT INTO `printers` VALUES (1,'Canon TS5000 series','Canon 01','','canon 01',1);
UNLOCK TABLES;


 
LOCK TABLES `games` WRITE; 
INSERT INTO `games` VALUES (1,'Voiture','','','minor'),(2,'Chapitau','','','minor'),(4,'Chapitaux','','','minor'); 
UNLOCK TABLES;


LOCK TABLES `gamepricing` WRITE; 
INSERT INTO `gamepricing` VALUES (14,1,21.00,'2024-01-07 21:10:52','2024-09-07 21:10:52'),(15,2,23.00,'2024-01-07 21:12:01','2024-09-07 21:12:01'),(16,4,23.00,'2024-01-07 21:12:09','2024-09-07 21:12:09');
UNLOCK TABLES;
 
LOCK TABLES `gameprinters` WRITE;
INSERT INTO `gameprinters` VALUES (1,1,'VoitureCanon',NULL),(2,1,'hb',NULL),(4,1,'Chap','');
UNLOCK TABLES;


LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES (8,'hmd','$2a$12$aZQT28mXrfcnGW.qzCr3e.E0aLvlRpPsMnTdnGJo7ObTq.fbQchLu',NULL),(9,'hamid','$2a$12$YKccz5barntLGLywFN3V7efdbREFao.5M4R29Z7p8yv/DxDerDf02',NULL);
UNLOCK TABLES;

LOCK TABLES `roles` WRITE;

INSERT INTO `roles` VALUES (1,'admin'),(2,'exploitant');
UNLOCK TABLES;



LOCK TABLES `permissions` WRITE;
INSERT INTO `permissions` VALUES (1,'gh');
UNLOCK TABLES;


LOCK TABLES `userroles` WRITE;

INSERT INTO `userroles` VALUES (8,1),(9,2);
UNLOCK TABLES;


LOCK TABLES `rolepermissions` WRITE;

UNLOCK TABLES;



LOCK TABLES `tickets` WRITE;
UNLOCK TABLES;


