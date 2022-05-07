var express = require('express');

let utilities = require('../utils/Utilities');
let databaseSql = require('../utils/DatabaseSql');

var router = express.Router();

router.post('/register', function (req, res) {
    if (!req.body.firstName || !req.body.lastName || !req.body.email || !req.body.username || !req.body.password) {
        res.status(400).json({ message: "Missing required parameters" });
    } else {
        var passwordData = utilities.saltHashPassword(req.body.password);
        var firstName = req.body.firstName;
        var lastName = req.body.lastName;

        var email = req.body.email;
        var username = req.body.username;

        var password = passwordData.passwordHash;
        var salt = passwordData.passwordSalt;

        databaseSql.handleCheckUsernameOrEmailSQL(req.body.username, req.body.email)
            .then(unused => {
                databaseSql.handleRegisterUserSQL(firstName, lastName, email, username, password, salt)
                    .then(ok => { res.status(200).json(ok); })
                    .catch(err => { res.status(500).json({ message: err }); })
            })
            .catch(err => {
                res.status(500).json({ message: err });
            });
    }
});

router.post('/login', function (req, res) {
    if (!req.body.username || !req.body.password) {
        res.status(400).json({ message: "Missing required parameters" });
    } else {
        function checkPassword(accountInformation) {
            var passwordDb = accountInformation['password'];
            var salt = accountInformation['salt']

            var passwordData = utilities.hashPasswordWithHmac(req.body.password, salt);

            if (passwordData.passwordHash != passwordDb) {
                return Promise.reject("Username or password is incorrect!");
            } else {
                return Promise.resolve({
                    firstName: accountInformation['first_name'],
                    lastName: accountInformation['last_name'],
                    email: accountInformation['email'],
                    accountId: accountInformation['accountId'],
                })
            }

            return Promise.reject("Unknown error");
        }

        databaseSql.handleUseLoginSQL(req.body.username)
            .then(checkPassword)
            .then(ok => { res.status(200).json(ok); })
            .catch(err => { res.status(500).json({ message: err }); });
    }
});

module.exports = router;