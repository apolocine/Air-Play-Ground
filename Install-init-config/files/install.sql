CREATE TABLE IF NOT EXISTS printers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    location VARCHAR(100),
    status VARCHAR(20) 
    );
    
    CREATE TABLE IF NOT EXISTS `tb_utilisateur` (
				  `id` int(11) NOT NULL AUTO_INCREMENT,  `nom` varchar(250) NOT NULL,
				  `prenom` varchar(250) NOT NULL,  `naissance` date NOT NULL,
				  `username` varchar(250) NOT NULL,  `password` varchar(250) DEFAULT NULL,
				  `fonction` varchar(250) DEFAULT NULL,  `description` varchar(250) DEFAULT NULL,
				  PRIMARY KEY (`id`),  UNIQUE KEY `username` (`username`)
				) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
				
INSERT INTO `tb_utilisateur` (`id`, `nom`, `prenom`, `naissance`, `username`, `password`, `fonction`, `description`) VALUES
				(1, 'MADANI', 'Hamid', '1972-10-03', 'drmdh@msn.com', 'azerty@26', 'Ophtalmologue', 'PoXviCZNZBHGyTsac6XdKQ==:lTbS2WuC5p/tMyEVhLPfNA==');