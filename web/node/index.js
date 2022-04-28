const express = require('express')

var cookieParser = require('cookie-parser')
var bodyParser = require('body-parser');
var multer = require('multer');
var upload = multer();

const app = express()
const port = process.env.PORT || 3000

app.use(cookieParser());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(upload.array());

app.get('/test', (req, res) => {
    res.send(saltHashPassword(''));
})

//Require the Router we defined in movies.js
var api = require('./api/api.js');

//Use the Router on the sub route /api
app.use('/api', api);

app.get('/', (req, res) => {
    res.send('Hello World!')
})

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
})
