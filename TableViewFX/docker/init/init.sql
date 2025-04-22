-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Hôte : db:3306
-- Généré le : mar. 22 avr. 2025 à 09:30
-- Version du serveur : 11.7.2-MariaDB-ubu2404
-- Version de PHP : 8.2.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

--
-- Base de données : `cvven`
--

-- --------------------------------------------------------

--
-- Structure de la table `logements`
--

CREATE TABLE `logements` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `chambres` int(11) NOT NULL,
  `salles_de_bain` int(11) NOT NULL,
  `prix_par_nuit` decimal(10,2) NOT NULL,
  `image` varchar(255) NOT NULL,
  `date_ajout` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `logements`
--

INSERT INTO `logements` (`id`, `nom`, `description`, `chambres`, `salles_de_bain`, `prix_par_nuit`, `image`, `date_ajout`) VALUES
(1, 'Château Royal', 'Château du 18ᵉ siècle, rénové, avec 10 chambres, salle de bal et jardin paysager.', 10, 7, 2500.00, '1.jpg', '2025-02-18 14:19:54'),
(2, 'Chalet Alpin Prestige', 'Chalet luxueux de 6 chambres avec spa privé et cheminée centrale.', 6, 4, 1800.00, '2.webp', '2025-02-18 14:19:54'),
(3, 'Villa Azure', 'Villa moderne avec 5 chambres, piscine à débordement et accès privé à la plage.', 5, 5, 3200.00, '3.jpg', '2025-02-18 14:19:54'),
(4, 'Manoir Élégance', 'Manoir élégant de 8 chambres avec parc arboré et sauna privé.', 8, 6, 2700.00, '4.jpg', '2025-02-18 14:19:54');

-- --------------------------------------------------------

--
-- Structure de la table `reservations`
--

CREATE TABLE `reservations` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `logement_id` int(11) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  `prix_total` decimal(10,2) NOT NULL,
  `date_reservation` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `reservations`
--

INSERT INTO `reservations` (`id`, `user_id`, `email`, `logement_id`, `nom`, `prenom`, `date_debut`, `date_fin`, `prix_total`, `date_reservation`) VALUES
(4, 11, '', 4, 'oui', 'prout', '2025-03-22', '2025-03-30', 20000.00, '2025-03-24 19:11:07');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `role` varchar(20) DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`, `created_at`, `role`) VALUES
(11, 'test', 'test@gmail.com', '$2y$10$JMdTMhEhb4BSW0JPqSnmlOCpCh0cGaZGrgsOLSCVH2wDA4P7XBta6', '2025-03-24 17:56:52', 'user'),
(13, 'admin', 'admin@cvven.com', '$2y$10$BU.uQLK7tw7xi57jZNc6LuW7oVCPmOt.D20U/tAkk6JHTtR/qrxFm', '2025-03-24 19:47:41', 'admin');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `logements`
--
ALTER TABLE `logements`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `reservations`
--
ALTER TABLE `reservations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `logement_id` (`logement_id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `logements`
--
ALTER TABLE `logements`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`logement_id`) REFERENCES `logements` (`id`);
COMMIT;
