
const express = require("express");
const jwt = require("jsonwebtoken");
const cors = require('cors');
const bodyParser = require('body-parser');

const PORT = process.env.PORT || 3001;
const app = express();
const axios = require('axios');

require("dotenv").config();

app.listen(PORT, () => {
    console.log(`Server listening on ${PORT}`);
});


app.use(cors());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json());

app.post("/api", (req, res) => {
    const phone = req.body.phone;
    //creating token with phone number
    const token = jwt.sign({ phone }, process.env.TOKEN_SECRET, { expiresIn: 300 });
    
    //sending newly created token to the middleman signed with key1
    axios.post('http://localhost:8080/middlemanapp/tokenHandler', {
        token: token,
    })
    .then((response) => {
    //recieved token from middleman which holds token to be decoded : response.headers.token
    console.log("🟡landing app recieved JWT from middleman: "+response.headers.token);

    jwt.verify(response.headers.token, process.env.TOKEN_SECRET, (err, decoded) => {
        if (err) {
            console.log("🔴error decoding token in landing app! " + err);
        }
        else {
            console.log("🟡landing app decoded token, status: "+decoded.status);
            //send status as response to landing frontend
            res.json({ status: decoded.status });
        }
    })

    }, (error) => {
        console.log(error);
    });
});

