<?php
$servername = "localhost";
$username = "root"; // Remplacez par votre nom d'utilisateur pour la base de données
$password = ""; // Remplacez par votre mot de passe pour la base de données
$dbname = "dbprinters"; // Remplacez par le nom de votre base de données



?>

<?php
// $servername = "localhost";
// $username = "username"; // Votre nom d'utilisateur pour la base de données
// $password = "password"; // Votre mot de passe pour la base de données
// $dbname = "myDB"; // Nom de votre base de données

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // 1. Nombre total de tickets par utilisateur
    echo "<h2>Nombre total de tickets par utilisateur</h2>";

    // Dernier jour
    $stmt1 = $conn->prepare("SELECT UserID, COUNT(*) AS TotalTickets FROM Tickets WHERE DATE(ValidDate) = CURDATE() - INTERVAL 1 DAY GROUP BY UserID");
    $stmt1->execute();
    $ticketsYesterday = $stmt1->fetchAll(PDO::FETCH_ASSOC);

    echo "<h3>Dernier jour</h3>";
    foreach ($ticketsYesterday as $ticket) {
        echo "UserID: " . $ticket["UserID"] . " - TotalTickets: " . $ticket["TotalTickets"] . "<br>";
    }

    // Dernière heure
    $stmt2 = $conn->prepare("SELECT UserID, COUNT(*) AS TotalTickets FROM Tickets WHERE ValidDate >= NOW() - INTERVAL 1 HOUR GROUP BY UserID");
    $stmt2->execute();
    $ticketsLastHour = $stmt2->fetchAll(PDO::FETCH_ASSOC);

    echo "<h3>Dernière heure</h3>";
    foreach ($ticketsLastHour as $ticket) {
        echo "UserID: " . $ticket["UserID"] . " - TotalTickets: " . $ticket["TotalTickets"] . "<br>";
    }

    // Total général
    $stmt3 = $conn->prepare("SELECT UserID, COUNT(*) AS TotalTickets FROM Tickets GROUP BY UserID");
    $stmt3->execute();
    $totalTickets = $stmt3->fetchAll(PDO::FETCH_ASSOC);

    echo "<h3>Total général</h3>";
    foreach ($totalTickets as $ticket) {
        echo "UserID: " . $ticket["UserID"] . " - TotalTickets: " . $ticket["TotalTickets"] . "<br>";
    }

    // 2. Total des prix par utilisateur
   // 2. Total des prix par utilisateur
echo "<h2>Total des prix par utilisateur</h2>";

// Dernier jour
$stmt4 = $conn->prepare("SELECT t.UserID, SUM(gp.Price) AS TotalPrice FROM Tickets t JOIN GamePricing gp ON t.GamePricingID = gp.PricingID WHERE DATE(t.ValidDate) = CURDATE() - INTERVAL 1 DAY GROUP BY t.UserID");
$stmt4->execute();
$totalPriceYesterday = $stmt4->fetchAll(PDO::FETCH_ASSOC);

echo "<h3>Dernier jour</h3>";
foreach ($totalPriceYesterday as $price) {
    echo "UserID: " . $price["UserID"] . " - TotalPrice: " . $price["TotalPrice"] . "<br>";
}
  // Dernière heure
$stmt5 = $conn->prepare("SELECT t.UserID, SUM(gp.Price) AS TotalPrice FROM Tickets t JOIN GamePricing gp ON t.GamePricingID = gp.PricingID WHERE t.ValidDate >= NOW() - INTERVAL 1 HOUR GROUP BY t.UserID");
$stmt5->execute();
$totalPriceLastHour = $stmt5->fetchAll(PDO::FETCH_ASSOC);

echo "<h3>Dernière heure</h3>";
foreach ($totalPriceLastHour as $price) {
    echo "UserID: " . $price["UserID"] . " - TotalPrice: " . $price["TotalPrice"] . "<br>";
}

// 3. Nombre de tickets par jeu
echo "<h2>Nombre de tickets par jeu</h2>";

// Dernier jour
$stmt6 = $conn->prepare("SELECT g.GameID, COUNT(*) AS TotalTickets FROM Tickets t JOIN Games g ON t.GameID = g.GameID WHERE DATE(t.ValidDate) = CURDATE() - INTERVAL 1 DAY GROUP BY g.GameID");
$stmt6->execute();
$ticketsPerGameYesterday = $stmt6->fetchAll(PDO::FETCH_ASSOC);

echo "<h3>Dernier jour</h3>";
foreach ($ticketsPerGameYesterday as $game) {
    echo "GameID: " . $game["GameID"] . " - TotalTickets: " . $game["TotalTickets"] . "<br>";
}

// Dernière heure
$stmt7 = $conn->prepare("SELECT g.GameID, COUNT(*) AS TotalTickets FROM Tickets t JOIN Games g ON t.GameID = g.GameID WHERE t.ValidDate >= NOW() - INTERVAL 1 HOUR GROUP BY g.GameID");
$stmt7->execute();
$ticketsPerGameLastHour = $stmt7->fetchAll(PDO::FETCH_ASSOC);

echo "<h3>Dernière heure</h3>";
foreach ($ticketsPerGameLastHour as $game) {
    echo "GameID: " . $game["GameID"] . " - TotalTickets: " . $game["TotalTickets"] . "<br>";
}

// Total général
$stmt8 = $conn->prepare("SELECT g.GameID, COUNT(*) AS TotalTickets FROM Tickets t JOIN Games g ON t.GameID = g.GameID GROUP BY g.GameID");
$stmt8->execute();
$totalTicketsPerGame = $stmt8->fetchAll(PDO::FETCH_ASSOC);

echo "<h3>Total général</h3>";
foreach ($totalTicketsPerGame as $game) {
    echo "GameID: " . $game["GameID"] . " - TotalTickets: " . $game["TotalTickets"] . "<br>";
}

    // 3. Nombre de tickets par jeu
    echo "<h2>Nombre de tickets par jeu</h2>";

    // ... Répétez les étapes de préparation, exécution et affichage pour les requêtes de tickets par jeu

    // Fermer la connexion
    $conn = null;

} catch(PDOException $e) {
    echo "Erreur : " . $e->getMessage();
}
?>

<?php
// $servername = "localhost";
// $username = "username"; // Remplacez par votre nom d'utilisateur
// $password = "password"; // Remplacez par votre mot de passe
// $dbname = "myDB"; // Remplacez par le nom de votre base de données

try {
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Nombre de tickets pour le jour en cours
    $stmt4 = $conn->prepare("SELECT COUNT(*) AS total_today FROM Tickets WHERE DATE(ValidDate) = CURDATE()");
    $stmt4->execute();
    $result1 = $stmt4->fetch(PDO::FETCH_ASSOC);
    $totalToday = $result1['total_today'];

    // Nombre de tickets pour la dernière heure
    $stmt2 = $conn->prepare("SELECT COUNT(*) AS total_last_hour FROM Tickets WHERE ValidDate >= NOW() - INTERVAL 1 HOUR");
    $stmt2->execute();
    $result2 = $stmt2->fetch(PDO::FETCH_ASSOC);
    $totalLastHour = $result2['total_last_hour'];

    // Affichage des résultats
    echo "<div>Nombre de Tickets pour le jour en cours : $totalToday</div>";
    echo "<div>Nombre de Tickets pour la dernière heure : $totalLastHour</div>";
    
} catch(PDOException $e) {
    echo "Erreur : " . $e->getMessage();
}

// $conn = null;
?>



<?php
// $servername = "localhost";
// $username = "username"; // Votre nom d'utilisateur pour la base de données
// $password = "password"; // Votre mot de passe pour la base de données
// $dbname = "myDB"; // Nom de votre base de données

try {
    // $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    // $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Exécuter et afficher chaque requête une par une

    // 1. Nombre total de tickets par utilisateur
    echo "<h2>Nombre total de tickets par utilisateur</h2>";

    // Dernier jour
    $stmt = $conn->prepare("SELECT UserID, COUNT(*) AS TotalTickets FROM Tickets WHERE DATE(ValidDate) = CURDATE() - INTERVAL 1 DAY GROUP BY UserID");
    $stmt->execute();
    $ticketsYesterday = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo "<h3>Dernier jour</h3>";
    foreach ($ticketsYesterday as $ticket) {
        echo "UserID: " . $ticket["UserID"] . " - TotalTickets: " . $ticket["TotalTickets"] . "<br>";
    }

    // ... Répétez pour les autres requêtes

    // Fermer la connexion
    // $conn = null;

} catch(PDOException $e) {
    echo "Erreur : " . $e->getMessage();
}
?>



<?php
// Création de la connexion
$conn_ = new mysqli($servername, $username, $password, $dbname);

// Vérification de la connexion
if ($conn_->connect_error) {
    die("Connection failed: " . $conn_->connect_error);
}

$sql = "SELECT * FROM Tickets";
$result = $conn_->query($sql);

if ($result->num_rows > 0) {
    // Affichage des données de chaque ligne
	 // " - Barcode: " . $row["Barcode"].
    while($row = $result->fetch_assoc()) {
        echo "TicketID: " . $row["TicketID"]. " - GameID: " . $row["GameID"]. " - GamePricingID: " . $row["GamePricingID"]. " - UserID: " . $row["UserID"]. " - ValidDate: " . $row["ValidDate"]. "<br>";
    }
} else {
    echo "0 results";
}

?>


<?php
$conn_->close();
  $conn = null;
?>