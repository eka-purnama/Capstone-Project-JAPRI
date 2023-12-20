const express = require('express');
const userRoutes = require('./routes/userRoutes');
const authRoutes = require('./routes/authRoutes');
const formJasaRoutes = require('./routes/formJasaRoutes');
const dbMiddleware = require('./middleware/db');
const authenticateToken = require('./middleware/authenticateToken');

const app = express();
const port = 5000;

app.use(express.json());
app.use(dbMiddleware);
app.use('/users', authenticateToken, userRoutes);
app.use('/auth', authRoutes);
app.use('/jasa', authenticateToken, formJasaRoutes);

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
