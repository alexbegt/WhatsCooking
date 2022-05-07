var Connection = require('tedious').Connection;
var Request = require('tedious').Request
var TYPES = require('tedious').TYPES;

/*
    Fetch recipies based on account
*/
const handleFetchRecipes = (accountId) => new Promise((resolve, reject) => {
    var recipeList = [];

    const connection = new Connection({
        server: 'whats-cooking.database.windows.net',

        authentication: {
            type: 'default',

            options: {
                userName: 'abehrhof',
                password: 'E9RE8ih!fBaE9P$^*5z$Ztr*'
            }
        },

        options: {
            encrypt: true,
            database: 'whats-cooking',
            useColumnNames: true
        }
    });

    var request = new Request("SELECT recipeId, recipeName, ingredients, instructions, imageData, category, categoryTag FROM DBO.recipes WHERE authorId = @AuthorId ", function (err, rowCount) {
        if (err) {
            console.log("Request error: " + err);

            reject(err);
        } else {
            if (recipeList.length === 0) reject("No recipes exist");

            resolve({
                recipes: recipeList
            });
        }
    });

    request.addParameter('AuthorId', TYPES.Int, accountId);

    request.on('row', function (columns) {
        recipeList.push({
            recipeId: columns['recipeId'].value,
            recipeName: columns['recipeName'].value,
            ingredients: columns['ingredients'].value,
            instructions: columns['instructions'].value,
            imageData: columns['imageData'].value,
            category: columns['category'].value,
            categoryTag: columns['categoryTag'].value,
            authorId: accountId
        });
    });

    connection.on('connect', err => {
        if (err) {
            console.log("Connect error: " + err);

            reject(err);
        }
        else {
            connection.execSql(request);
        }
    });

    connection.connect();
});

/*
    Fetch recipes for all acccounts
*/
const handleFetchAllRecipes = () => new Promise((resolve, reject) => {
    var recipeList = [];

    const connection = new Connection({
        server: 'whats-cooking.database.windows.net',

        authentication: {
            type: 'default',

            options: {
                userName: 'abehrhof',
                password: 'E9RE8ih!fBaE9P$^*5z$Ztr*'
            }
        },

        options: {
            encrypt: true,
            database: 'whats-cooking',
            useColumnNames: true
        }
    });

    var request = new Request("SELECT * FROM DBO.recipes", function (err, rowCount) {
        if (err) {
            console.log("Request error: " + err);

            reject({
                successful: true,
                recipes: [],
                message: err
            });
        } else {
            if (recipeList.length === 0) reject({
                successful: true,
                recipes: [],
                message: "No recipes exist"
            });

            console.log(recipeList);

            resolve({
                successful: true,
                recipes: recipeList,
                message: ''
            });
        }
    });

    request.on('row', function (columns) {
        recipeList.push({
            recipeId: columns['recipeId'].value,
            recipeName: columns['recipeName'].value,
            ingredients: columns['ingredients'].value,
            instructions: columns['instructions'].value,
            imageData: columns['imageData'].value,
            category: columns['category'].value,
            categoryTag: columns['categoryTag'].value,
            authorId: columns['authorId'].value
        });
    });

    connection.on('connect', err => {
        if (err) {
            console.log("Connect error: " + err);

            reject({
                successful: true,
                recipes: [],
                message: err
            });
        }
        else {
            connection.execSql(request);
        }
    });

    connection.connect();
})

/*
    Add recipes to database
*/
const handleAddRecipe = (recipeName, ingredients, instructions, imageData, category, categoryTag, authorId) => new Promise((resolve, reject) => {
    var recipeInformation = {};

    const connection = new Connection({
        server: 'whats-cooking.database.windows.net',

        authentication: {
            type: 'default',

            options: {
                userName: 'abehrhof',
                password: 'E9RE8ih!fBaE9P$^*5z$Ztr*'
            }
        },

        options: {
            encrypt: true,
            database: 'whats-cooking'
        }
    });

    var request = new Request("INSERT INTO DBO.recipes (recipeName, ingredients, instructions, imageData, category, categoryTag, authorId) OUTPUT INSERTED.recipeId VALUES (@RecipeName, @Ingredients, @Instructions, @ImageData, @Category, @CategoryTag, @AuthorId)", function (err, rowCount) {
        if (err) {
            console.log("Request error: " + err);

            reject(err);
        } else {
            if (Object.keys(recipeInformation).length === 0) reject("Unable to save recipe, Please contact the administrator");

            resolve({ successful: true });
        }
    });

    request.addParameter('RecipeName', TYPES.VarChar, recipeName);
    request.addParameter('Ingredients', TYPES.NVarChar, ingredients);
    request.addParameter('Instructions', TYPES.NVarChar, instructions);
    request.addParameter('ImageData', TYPES.NVarChar, imageData);
    request.addParameter('Category', TYPES.NVarChar, category);
    request.addParameter('CategoryTag', TYPES.NVarChar, categoryTag);
    request.addParameter('AuthorId', TYPES.Int, authorId);

    request.on('row', function (columns) {
        columns.forEach(function (column) {
            if (column.value !== null) {
                recipeInformation[column.metadata.colName] = column.value;
            }
        });
    });

    connection.on('connect', err => {
        if (err) {
            console.log("Connect error: " + err);

            reject(err);
        }
        else {
            connection.execSql(request);
        }
    });

    connection.connect();
});

/*
    Fetch recipies based on account
*/
const handleFetchRecipeImage = (recipeId) => new Promise((resolve, reject) => {
    var recipeImageData = {};

    const connection = new Connection({
        server: 'whats-cooking.database.windows.net',

        authentication: {
            type: 'default',

            options: {
                userName: 'abehrhof',
                password: 'E9RE8ih!fBaE9P$^*5z$Ztr*'
            }
        },

        options: {
            encrypt: true,
            database: 'whats-cooking',
            useColumnNames: true
        }
    });

    var request = new Request("SELECT imageData FROM DBO.recipes WHERE recipeId = @RecipeId ", function (err, rowCount) {
        if (err) {
            console.log("Request error: " + err);

            reject(err);
        } else {
            if (recipeImageData.length === 0) reject("No Recipe Image exists");

            resolve(recipeImageData);
        }
    });

    request.addParameter('RecipeId', TYPES.Int, recipeId);

    request.on('row', function (columns) {
        recipeImageData['imageData'] = columns['imageData'].value;
    });

    connection.on('connect', err => {
        if (err) {
            console.log("Connect error: " + err);

            reject(err);
        }
        else {
            connection.execSql(request);
        }
    });

    connection.connect();
});

/*
    Add's recipe to favorites
*/
const handleAddRecipeToFavorites = (recipeId, authorId) => new Promise((resolve, reject) => {

});

module.exports.handleFetchRecipes = handleFetchRecipes;
module.exports.handleFetchAllRecipes = handleFetchAllRecipes;

module.exports.handleAddRecipe = handleAddRecipe;
module.exports.handleFetchRecipeImage = handleFetchRecipeImage;
module.exports.handleAddRecipeToFavorites = handleAddRecipeToFavorites;