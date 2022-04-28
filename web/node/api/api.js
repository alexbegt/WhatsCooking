var express = require('express');

let helper = require('../utils/helpers');

var router = express.Router();

var Connection = require('tedious').Connection;
var Request = require('tedious').Request
var TYPES = require('tedious').TYPES;

const executeRegisterSQL = (firstName, lastName, email, username, password, salt) => new Promise((resolve, reject) => {
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
            console.log("request error: " + err);
            reject(err);
        } else {
            console.log(accountInformation);
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
            console.log("connection error: " + err);
            reject(err);
        }
        else {
            connection.execSql(request);
        }
    });

    connection.connect();
});

const executeCheckExistsSQL = (username, email) => new Promise((resolve, reject) => {
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
            console.log(err);
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
            console.log(err);
            reject(err);
        }
        else {
            connection.execSql(request);
        }
    });

    connection.connect();
});

/* Handle SQL for Login */

const executeLoginSql = (username) => new Promise((resolve, reject) => {
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
            reject(err);
        }
        else {
            connection.execSql(request);
        }
    });

    connection.connect();
});

router.post('/login', function (req, res) {
    if (!req.body.username || !req.body.password) {
        res.status(400).json({ message: "Missing required parameters" });
    } else {
        function checkPassword(accountInformation) {
            var passwordDb = accountInformation['password'];
            var salt = accountInformation['salt']

            var passwordData = helper.hashPasswordWithHmac(req.body.password, salt);

            if (passwordData.passwordHash != passwordDb) {
                return Promise.reject("Username or password is incorrect!");
            } else {
                return Promise.resolve({
                    firstName: accountInformation['first_name'],
                    lastName: accountInformation['last_name'],
                    email: accountInformation['email'],
                    accountId: accountInformation['uniqueId'],
                })
            }

            return Promise.reject("Unknown error");
        }

        executeLoginSql(req.body.username)
            .then(checkPassword)
            .then(ok => { res.status(200).json(ok); })
            .catch(err => { res.status(500).json({ message: err }); });
    }
});

router.post('/register', function (req, res) {
    if (!req.body.firstName || !req.body.lastName || !req.body.email || !req.body.username || !req.body.password) {
        res.status(400).json({ message: "Missing required parameters" });
    } else {
        var passwordData = helper.saltHashPassword(req.body.password);
        var firstName = req.body.firstName;
        var lastName = req.body.lastName;

        var email = req.body.email;
        var username = req.body.username;

        var password = passwordData.passwordHash;
        var salt = passwordData.passwordSalt;

        executeCheckExistsSQL(req.body.username, req.body.email)
            .then(unused => {
                executeRegisterSQL(firstName, lastName, email, username, password, salt)
                    .then(ok => { res.status(200).json(ok); })
                    .catch(err => { res.status(500).json({ message: err }); })
            })
            .catch(err => {
                res.status(500).json({ message: err });
            });
    }
});

module.exports = router;
