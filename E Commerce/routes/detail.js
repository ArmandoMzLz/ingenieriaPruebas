const express = require('express');
const router = express.Router();

router.get('/', (req, res) => {
    res.render('./Detail');
});

module.exports = router;