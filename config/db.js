const path = require('path');
const Firestore = require('@google-cloud/firestore');

const keyFilename = path.join(__dirname, 'db.json');

const db = new Firestore({
  projectId: 'japri-dev',
  keyFilename: keyFilename,
});

module.exports = db;
