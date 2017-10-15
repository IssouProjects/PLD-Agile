# Bonnes pratiques

On fonction avec du _feature branching_, c'est à dire que l'on va créer une branche par tâche. Une fois cette tâche implémentée, elle est revue et intégrée dans `master`.

## Outils
- Git en ligne de commande

ou

- GitHub Desktop desktop.github.com

>Je conseille VRAIMENT Desktop parce que c'est plus visuel et c'est plus difficile de faire des bêtises. Déso pas déso les puristes de la ligne de commande.

## Workflow
Si vous avez à faire une tâche, voici les étapes à faire :

1. Aller sur `master` et récupérer les dernières modifs :
```
git checkout master
git fetch
git pull
```

2. Créer une branche localement
`git checkout -b nom_branche` ou Branch > New branch dans GitHub Desktop
> Ne pas mettre d'accents, d'espaces ou de caractères spéciaux autres que `-` ou `_` dans le nom des branches

3. Faire des petits commits pour chaque modification. Eviter les commits avec masse de modifications. 
>Ca nuit à la lisibilité de l'historique et c'est plus compliqué à revert en cas de problème. Dans Desktop, il est possible de cocher les fichiers et les lignes à commiter. En ligne de commande, utiliser `git add` et `cherry-pick`.

4. Une fois que vous avez fini une session de travail, faites un `git push`. Ca a pour effet de mettre tous vos commits sur GitHub.
> Pas la peine de push à chaque commit. Garder son travail uniquement localement le temps qu'on travaille permet de revenir plus facilement en arrière, annuler des commits, etc...

4. Lorsque vous avez besoin d'aide, fini le boulot ou quoique ce soit qui nécessite une discussion sur ce que vous avez fait, ouvrez une pull request.
Une pull request permet la discussion sur les modifications, elle ne sert pas _juste_ à faire des merge.

>Il est toujours possible de push des commits après avoir ouvert une pull request

5. Pour terminer, ces modifications peuvent être merged dans `master` si le code compile, les tests passent et qu'au moins une personne a approuvé ces changements et que personne n'a demandé de changement.

## En général

- Ne jamais faire de modifications sur `master`
- Ne jamais faire de `rebase` pour récupérer les commits d'une branche autre que `master`. S'il y en a vraiment besoin, ce n'est probablement pas le bon moment de travailler sur cette modification maintenant.
- S'assurer que **personne** d'autre ne travaille sur la même branche en même temps

## FAQ

### J'ai créé ma branche, mais depuis de nouveaux commits sont sur `master`, comment je fais pour les récupérer dans ma branche ?

Faire un push de la branche à mettre à jour avant (au cas-où ça se passe mal)

``` bash
# On met à jour master avec les derniers commits
# IMPORTANT
git checkout master
git fetch
git pull

## On récupère les commits de master dans la branche
git checkout branche_a_mettre_a_jour
git merge master
```

_En savoir plus ici : https://guides.github.com/introduction/flow/_
