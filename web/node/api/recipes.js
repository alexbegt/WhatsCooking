var express = require('express');

let databaseSql = require('../utils/RecipesDatabaseSql');

var router = express.Router();

/*
    Fetch recipes based on account
*/
router.post('/getAllRecipesByAccount', function (req, res) {
    if (!req.body.accountId) {
        res.status(400).json({ message: "Missing required parameters" });
    } else {
        databaseSql.handleFetchRecipesByAccount(req.body.accountId)
            .then(ok => { res.status(200).json(ok); })
            .catch(err => { res.status(200).json(err); });
    }
});

/*
    Fetch recipes
*/
router.post('/getAllRecipes', function (req, res) {
    databaseSql.handleFetchAllRecipes()
        .then(ok => { res.status(200).json(ok); })
        .catch(err => { res.status(200).json(err); });
});

/*
    Add recipe
*/
router.post('/addRecipe', function (req, res) {
    if (!req.body.recipeName || !req.body.ingredients || !req.body.instructions || !req.body.imageData || !req.body.category || !req.body.categoryTag || !req.body.authorId) {
        res.status(400).json({ message: "Missing required parameters" });
    } else {
        databaseSql.handleAddRecipe(req.body.recipeName, req.body.ingredients, req.body.instructions, req.body.imageData, req.body.category, req.body.categoryTag, req.body.authorId)
            .then(ok => { res.status(200).json(ok); })
            .catch(err => { res.status(500).json({ message: err }); });
    }
});

/*
    Fetch recipe image for recipe Card
*/
router.post('/getRecipeImage', function (req, res) {
    if (!req.body.recipeId) {
        res.status(400).json({ message: "Missing required parameters" });
    } else {
        databaseSql.handleFetchRecipeImage(req.body.recipeId)
            .then(ok => { res.status(200).json(ok); })
            .catch(err => { res.status(500).json({ message: err }); });
    }
});

/*
    Adds recipe to user favorites
*/
router.post('/addRecipeToFavorites', function (req, res) {
    if (!req.body.recipeId || !req.body.authorId) {
        res.status(400).json({ message: "Missing required parameters" });
    } else {
        databaseSql.handleCheckRecipeAlreadyFavorited(req.body.recipeId, req.body.authorId)
            .then(() => {
                databaseSql.handleAddRecipeToFavorites(req.body.recipeId, req.body.authorId)
                    .then(ok => { res.status(200).json(ok); })
                    .catch(err => { res.status(500).json({ message: err }); })
            })
            .catch(err => {
                res.status(500).json({ message: err });
            });
    }
});

/*
    Fetch recipes based on account
*/
router.post('/getAllFavoriteRecipesByAccount', function (req, res) {
    if (!req.body.accountId) {
        res.status(400).json({ message: "Missing required parameters" });
    } else {
        databaseSql.handleFetchFavoriteRecipesByAccount(req.body.accountId)
            .then(ok => { res.status(200).json(ok); })
            .catch(err => { res.status(200).json(err); });
    }
});

module.exports = router;