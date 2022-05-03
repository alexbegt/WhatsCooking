var Connection = require('tedious').Connection;
var Request = require('tedious').Request
var TYPES = require('tedious').TYPES;

/*
    Checks if the user with the given username or email exists
*/
const handleCheckUsernameOrEmailSQL = (username, email) => new Promise((resolve, reject) => {
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

    var request = new Request("SELECT COUNT(*) FROM DBO.users WHERE username = @Username OR email = @Email", function (err, rowCount) {
        if (err) {
            console.log("Request error: " + err);

            reject(err);
        } else {
            if (count === 0) resolve("No accounts exist");

            reject("Account already in use");
        }
    });

    request.addParameter('Username', TYPES.VarChar, username);
    request.addParameter('Email', TYPES.VarChar, email);

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

/*
    Handles the SQL for inserting a new user
*/
const handleRegisterUserSQL = (firstName, lastName, email, username, password, salt) => new Promise((resolve, reject) => {
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

    var accountInformation = {};

    var request = new Request("INSERT INTO DBO.users (first_name, last_name, email, username, password, salt) OUTPUT INSERTED.accountId VALUES (@FirstName, @LastName, @Email, @Username, @Password, @Salt)", function (err, rowCount) {
        if (err) {
            console.log("Request error: " + err);

            reject(err);
        } else {
            if (Object.keys(accountInformation).length === 0) reject("Could not create an account, please contact administrator");

            resolve(accountInformation);
        }
    });

    request.addParameter('FirstName', TYPES.VarChar, firstName);
    request.addParameter('LastName', TYPES.VarChar, lastName);
    request.addParameter('Email', TYPES.VarChar, email);
    request.addParameter('Username', TYPES.VarChar, username);
    request.addParameter('Password', TYPES.VarChar, password);
    request.addParameter('Salt', TYPES.VarChar, salt);

    request.on('row', function (columns) {
        columns.forEach(function (column) {
            if (column.value !== null) {
                accountInformation[column.metadata.colName] = column.value;
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
    Handle SQL for Login 
*/
const handleUseLoginSQL = (username) => new Promise((resolve, reject) => {
    var accountInformation = {};

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

    var request = new Request("SELECT email, first_name, last_name, password, salt, accountId FROM DBO.users WHERE username = @Username ", function (err, rowCount) {
        if (err) {
            console.log("Request error: " + err);

            reject(err);
        } else {
            if (Object.keys(accountInformation).length === 0) reject("Username does not exist!");

            resolve(accountInformation);
        }
    });

    request.addParameter('Username', TYPES.VarChar, username);

    request.on('row', function (columns) {
        columns.forEach(function (column) {
            if (column.value !== null) {
                accountInformation[column.metadata.colName] = column.value;
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

    var request = new Request("SELECT uniqueId, ingredients, instructions, recipeName, category FROM DBO.recipe WHERE authorId = @AuthorId ", function (err, rowCount) {
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
            uniqueId: columns['uniqueId'].value,
            ingredients: columns['ingredients'].value,
            instructions: columns['instructions'].value,
            recipeName: columns['recipeName'].value,
            category: columns['category'].value,
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
            if (recipeList.length === 0) reject("No recipes exist");

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
const handleAddRecipe = (recipeName, ingredients, instructions, imageData, category, authorId) => new Promise((resolve, reject) => {
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

    var request = new Request("INSERT INTO DBO.recipes (recipeName, ingredients, instructions, imageData, category, authorId) OUTPUT INSERTED.recipeId VALUES (@RecipeName, @Ingredients, @Instructions, @ImageData, @Category, @AuthorId)", function (err, rowCount) {
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


module.exports.handleCheckUsernameOrEmailSQL = handleCheckUsernameOrEmailSQL;
module.exports.handleRegisterUserSQL = handleRegisterUserSQL;
module.exports.handleUseLoginSQL = handleUseLoginSQL;

module.exports.handleFetchRecipes = handleFetchRecipes;
module.exports.handleFetchAllRecipes = handleFetchAllRecipes;
module.exports.handleAddRecipe = handleAddRecipe;