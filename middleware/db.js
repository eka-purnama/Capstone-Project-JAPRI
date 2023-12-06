const db = require('../config/db');

const dbMiddleware = (req, res, next) => {
  req.db = db;
  next();
};

module.exports = dbMiddleware;
