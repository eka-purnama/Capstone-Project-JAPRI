const path = require('path');
const { Storage } = require('@google-cloud/storage');

const keyFilename = path.join(__dirname, 'bucket.json');

const storage = new Storage({
  projectId: 'japri-dev',
  keyFilename: keyFilename,
});

module.exports = storage;
