const express = require('express')

var cookieParser = require('cookie-parser')
var bodyParser = require('body-parser');
var multer = require('multer');
var upload = multer();

const app = express()
const port = process.env.PORT || 3000

app.use(cookieParser());
app.use(bodyParser.json({ limit: '150mb', extended: true }));
app.use(bodyParser.urlencoded({ limit: '150mb', extended: true }));
app.use(upload.array());

app.get('/test', (req, res) => {
    res.send(saltHashPassword(''));
})

//Require the Router we defined in api.js
var api = require('./api/index.js');

//Use the Router on the sub route /api
app.use('/api', api);

//Require the Router we defined in movies.js
var recipesApi = require('./api/recipes.js');

//Use the Router on the sub route /api/recipes
app.use('/api/recipes', recipesApi);

app.get('/', (req, res) => {
    res.send('Hello World!')
})

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`)
})
