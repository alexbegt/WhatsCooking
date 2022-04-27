var Connection = require('tedious').Connection;

var config = {
    server: 'whats-cooking.database.windows.net',
    authentication: {
        type: 'default',
        options: {
            userName: 'abehrhof',
            password: 'E9RE8ih!fBaE9P$^*5z$Ztr*'
        }
    },
    options: {
        // If you are on Microsoft Azure, you need encryption:
        encrypt: true,
        database: 'whats-cooking'
    }
};

var connection = new Connection(config);

module.exports = connection;