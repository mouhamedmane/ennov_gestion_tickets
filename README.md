# Gestion de Tickets

Gestion de Tickets est une application Spring Boot conçue pour gérer des tickets d'incidents, assigner des utilisateurs, et suivre l'état des tickets dans un système d'information.

## Fonctionnalités

- Création, modification, suppression de tickets.
- Gestion des utilisateurs et assignation de tickets à des utilisateurs.
- Suivi de l'état des tickets avec les statuts : "en cours", "terminé", "annulé".
- Documentation de l'API via Swagger UI.

## Prérequis

- Java 17 ou plus récent
- Maven 3.6.0 ou plus récent

## Installation

## Cloner le projet
git clone https://github.com/mouhamedmane/ennov_gestion_tickets.git  
cd ennov_gestion_tickets

## Base de données H2
 base de données H2 en mémoire, accessible via le navigateur à l'URL http://localhost:8080/h2-console.  
 Pour vous connecter à la console H2, utilisez :  
 - l'URL : jdbc:h2:mem:test  
 - l'utilisateur : sa  
 - laissez le champ mot de passe vide.  

 ## Documentation
 La documentation de l'API est générée automatiquement via Swagger et est accessible à l'adresse suivante : http://localhost:8080/swagger-ui/index.html.  
 Voici quelques-uns des principaux points d'API fournis par cette application :

Le contrôleur UserController gère la récupération des utilisateurs, la création de nouveaux utilisateurs, et la mise à jour des informations utilisateur.  
Il comprend des endpoints comme :

- GET /api/users : récupérer tous les utilisateurs.
- POST /api/users : créer un nouvel utilisateur.
- PUT /api/users/{id} : mettre à jour un utilisateur.
- GET /api/users/{id}/tickets : récupérer les tickets assignés à un utilisateur donné.

Le TicketController gère les tickets dans le système. Il permet de créer, mettre à jour, récupérer et supprimer des tickets. Voici quelques endpoints proposés :

- GET /api/tickets : récupérer tous les tickets.
- POST /api/tickets : créer un nouveau ticket.
- PUT /api/tickets/{id} : mettre à jour un ticket.
- PUT /api/tickets/{id}/assign/{userId} : assigné un ticket à un utilisateur
- DELETE /api/tickets/{id} : supprimer un ticket.

