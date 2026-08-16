# Instructions permanentes du repository

## Source de vérité et informations industrielles

1. Le cahier des charges principal situé dans `docs/cahier_charge_PFA_YAZAKI.pdf` est la source principale de vérité du projet.
2. Ne jamais inventer d'informations industrielles concernant YAZAKI.
3. Toute information inconnue ou non validée doit être explicitement marquée : **« À confirmer avec l'encadrant »**.
4. Lorsque l'infrastructure réelle est inconnue, utiliser des interfaces, des configurations et des fake adapters afin de conserver une solution testable sans supposer le fonctionnement des systèmes réels.

## Méthode de développement

5. Développer une seule phase à la fois.
6. Avant chaque modification :
   - inspecter le repository ;
   - expliquer brièvement le travail prévu.
7. Après chaque modification :
   - compiler le projet ;
   - exécuter les tests pertinents ;
   - revoir le diff ;
   - vérifier l'absence de secrets et de données confidentielles.
8. Utiliser une architecture simple, testable et maintenable.
9. Ne pas ajouter RabbitMQ, Kafka, Kubernetes ou une architecture en microservices sans exigence préalablement validée.
10. Ne jamais effectuer de commit automatiquement sans demande explicite de l'utilisateur.

## Sécurité et confidentialité

11. Ne jamais ajouter au repository de mots de passe, tokens, clés privées, vraies adresses IP ou informations internes.

## Clôture de phase

12. À la fin de chaque phase, fournir un compte rendu avec les rubriques suivantes :
    - Implemented
    - Files created/modified
    - Tests
    - Decisions
    - À confirmer avec l'encadrant
    - Next step
13. S'arrêter après chaque phase majeure et attendre la validation explicite de l'utilisateur avant de poursuivre.
