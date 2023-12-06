const Firestore = require('@google-cloud/firestore');

const db = new Firestore({
  projectId: 'japri-dev',
  keyFilename: 'C:\\Users\\LENOVO\\Downloads\\RPL\\Code Test\\Simple-App\\japri-dev-abd8589ded6d.json',
});

module.exports = db;
