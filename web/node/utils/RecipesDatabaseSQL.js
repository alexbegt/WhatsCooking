var Connection = require('tedious').Connection;
var Request = require('tedious').Request
var TYPES = require('tedious').TYPES;

/*
    Fetch recipes based on account
*/
const handleFetchRecipesByAccount = (accountId) => new Promise((resolve, reject) => {
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

    var request = new Request("SELECT * FROM DBO.Recipes WHERE authorId = @AuthorId ", function (err, rowCount) {
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

            resolve({
                successful: true,
                recipes: recipeList,
                message: ''
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
    Fetch favorites based on account
*/
const handleFetchFavoriteRecipesByAccount = (accountId) => new Promise((resolve, reject) => {
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

    var request = new Request("SELECT * FROM DBO.Recipes WHERE recipeId IN (SELECT recipeId from RecipesFavoritedByUser AS fr where fr.accountId = @AuthorId)", function (err, rowCount) {
        if (err) {
            console.log(err);

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

            resolve({
                successful: true,
                recipes: recipeList,
                message: ''
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

    var request = new Request("SELECT * FROM DBO.Recipes", function (err, rowCount) {
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

    var request = new Request("INSERT INTO DBO.Recipes (recipeName, ingredients, instructions, imageData, category, categoryTag, authorId) OUTPUT INSERTED.recipeId VALUES (@RecipeName, @Ingredients, @Instructions, @ImageData, @Category, @CategoryTag, @AuthorId)", function (err, rowCount) {
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
    Fetch recipes based on account
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

    var request = new Request("SELECT imageData FROM DBO.Recipes WHERE recipeId = @RecipeId ", function (err, rowCount) {
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
            database: 'whats-cooking',
            useColumnNames: true
        }
    });

    var request = new Request("INSERT INTO DBO.RecipesFavoritedByUser (recipeId, accountId) OUTPUT INSERTED.uniqueId VALUES (@RecipeId, @AccountId)", function (err, rowCount) {
        if (err) {
            console.log("Request error: " + err);

            reject(err);
        } else {
            if (Object.keys(recipeInformation).length === 0) reject("Unable to favorite recipe, Please contact the administrator");

            resolve({ successful: true });
        }
    });

    request.addParameter('RecipeId', TYPES.Int, recipeId);
    request.addParameter('AccountId', TYPES.Int, authorId);

    request.on('row', function (columns) {
        recipeInformation['recipeId'] = columns['uniqueId'].value;
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
    Checks if the user has already favorited the recipe
*/
const handleCheckRecipeAlreadyFavorited = (recipeId, authorId) => new Promise((resolve, reject) => {
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

    var count = 0;

    var request = new Request("SELECT COUNT(*) FROM DBO.RecipesFavoritedByUser WHERE accountId = @AccountId AND recipeId = @RecipeId", function (err, rowCount) {
        if (err) {
            console.log("Request error: " + err);

            reject(err);
        } else {
            if (count === 0) resolve("Continue");

            reject("Recipe Already Added To Favorite");
        }
    });

    request.addParameter('AccountId', TYPES.Int, authorId);
    request.addParameter('RecipeId', TYPES.Int, recipeId);

    request.on('row', function (columns) {
        columns.forEach(function (column) {
            count = column.value;
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

module.exports.handleFetchRecipesByAccount = handleFetchRecipesByAccount;
module.exports.handleFetchFavoriteRecipesByAccount = handleFetchFavoriteRecipesByAccount;
module.exports.handleFetchAllRecipes = handleFetchAllRecipes;

module.exports.handleAddRecipe = handleAddRecipe;
module.exports.handleFetchRecipeImage = handleFetchRecipeImage;
module.exports.handleAddRecipeToFavorites = handleAddRecipeToFavorites;
module.exports.handleCheckRecipeAlreadyFavorited = handleCheckRecipeAlreadyFavorited;