# ParisJanitor - Microservice des utilisateurs

<!--Une brève description de votre projet -->
Ce microservice est une composante autonome conçue selon l'architecture en couche, 
il se concentre uniquement sur les fonctionnalités liées à la gestion des des profils utilisateurs,
des permissions & rôles et de la sécurité (Bacic Auth/JWT)

## Table des matières

1. [Introduction](#introduction)
2. [Prérequis](#prérequis)
3. [Architecture](#architecture)
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
13. [Dépannage](#Dépannage)


## Introduction

<!--Fournir une description plus détaillée de votre projet, en Expliquant les objectifs, les fonctionnalités principales et le contexte du projet.-->

## Prérequis

- Java 17
- Maven 3.9.9
- Spring Boot 3.3.5
- Base de données relationnelles: MySQL Workbench 8.0
- Spring Security (Basic Auth & JWT)

## Architecture


## Points de terminaison

### User

- POST /api/users : Créer un nouvel utilisateur.
- GET /api/users/{id} : Récupérer les détails d'un utilisateur.
- PUT /api/users/{id} : Mettre à jour les informations d'un utilisateur.
- DELETE /api/users/{id} : Supprimer un utilisateur.
- POST /api/login : Authentifier un utilisateur.


### Refresh token

- POST /api/user : Créer un nouvel utilisateur.
- GET /api/user/{id} : Récupérer les détails d'un utilisateur.
- PUT /api/users/{id} : Mettre à jour les informations d'un utilisateur.
- DELETE /api/users/{id} : Supprimer un utilisateur.
- POST /api/login : Authentifier un utilisateur.


### Session

- POST /api/session : Créer un nouvel utilisateur.
- GET /api/session/{id} : Récupérer les détails d'un utilisateur.
- PUT /api/session/{id} : Mettre à jour les informations d'un utilisateur.
- DELETE /api/session/{id} : Supprimer un utilisateur.
- POST /api/session : Authentifier un utilisateur.

## Tests

1 - Unitaires : Tester des morceaux isolés de code (services/repo)

2 - Intégration : Vérifier si les parties de l'app fonctionnent correctement ensemble (API,BD)




## Dépannage

#### 1. Récursion dans une structure de donnée

L'exception HttpMessageNotWritableException signifie que Spring Boot ne peut pas convertir ton objet en JSON pour la réponse HTTP. La cause exacte est indiquée :

"Document nesting depth (1001) exceeds the maximum allowed (1000)"

Cela veut dire que ton objet JSON a trop de niveaux imbriqués, ce qui peut être dû à :

Une relation bidirectionnelle infinie entre des entités (ex. : @OneToMany et @ManyToOne mal gérées).

Une récursion dans une structure de données.

Une mauvaise configuration de la sérialisation JSON.

#### Solution possible 
Utiliser @JsonIgnore ou @JsonManagedReference / @JsonBackReference

#### 2. Probleme de Centralisation de version dans Maven

Elle a rendu impossible l'affichage du Swagger.

"Unauthorized error: Full authentication is required" → Problème avec Spring Security, qui bloque l'accès à Swagger.

"java.lang.NoSuchMethodError: ControllerAdviceBean.<init>(Object)" → Problème de compatibilité des dépendances Spring.

#### Solution possible

Ajout de la section <properties> dans le fichier pom.xml de Maven ,
utilisée pour définir les variables (versions) réutilisables  
pour les autres dépendences du projet pour des raisons de compatibilités.



#### 3. L'erreur MalformedInputException: Input length = 1

Indique généralement un problème avec l'encodage des fichiers dans le projet, 
en particulier avec le fichier application.properties. Ce problème se produit 
souvent lorsque les caractères spéciaux ou l'encodage des fichiers ne sont pas 
correctement gérés.

#### Solution possible

S'Assurer que le fichier application.properties est encodé en UTF-8.