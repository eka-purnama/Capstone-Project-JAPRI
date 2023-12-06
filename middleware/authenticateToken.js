const jwt = require('jsonwebtoken');

const authenticateToken = (req, res, next) => {
  const token = req.headers.authorization;

  if (!token) {
    return res.status(401).json({ message: 'Unauthorized' });
  }

  const decoded = jwt.decode(token);

  jwt.verify(token, 'your-secret-key', (err, user) => {
    if (err) {
      if (err.name === 'TokenExpiredError') {
        return res.status(401).json({ message: 'Token has expired' });
      } else {
        return res.status(403).json({ message: 'Invalid token' });
      }
    }

    req.user = decoded;

    next();
  });
};

module.exports = authenticateToken;
