# ParisJanitor - Microservice des utilisateurs

<!--Une brève description de votre projet -->
Ce microservice est une composante autonome conçue selon l'architecture en couche, il se concentre uniquement sur les fonctionnalités liées à la gestion des utilisateurs.

## Table des matières

1. [Introduction](#introduction)
2. [Prérequis](#prérequis)
3. [Installation](#installation)
4. [Configuration](#configuration)
5. [Utilisation](#utilisation)
6. [Tests](#tests)
7. [Contribuer](#contribuer)
8. [Licence](#licence)
9. [Deploiement](#deploiement)
10. [Points](#Points de Terminaison API)
11. [Authentification](#Authentification)
12. [Exemples](#Exemples)
13. [Depannage](#Dépannage)


## Introduction

<!--Fournir une description plus détaillée de votre projet, en Expliquant les objectifs, les fonctionnalités principales et le contexte du projet.-->

## Prérequis

- Java 17
- Maven 3.9.9
- Spring Boot 3.3.5
- Base de données relationnelles: MySQL Workbench 8.0
- Spring Security (Basic Auth & JWT)

## Points de terminaison

- POST /users : Créer un nouvel utilisateur.
- GET /users/{id} : Récupérer les détails d'un utilisateur.
- PUT /users/{id} : Mettre à jour les informations d'un utilisateur.
- DELETE /users/{id} : Supprimer un utilisateur.
- POST /login : Authentifier un utilisateur.