var express = require('express');

let helper = require('../utils/helpers');

var router = express.Router();

var Connection = require('tedious').Connection;
var Request = require('tedious').Request
var TYPES = require('tedious').TYPES;

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

    var request = new Request("SELECT email, first_name, last_name, password, salt, uniqueId FROM DBO.users WHERE username = @Username ", function (err, rowCount) {
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

module.exports = router;