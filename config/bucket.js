const { Storage } = require('@google-cloud/storage');

const storage = new Storage({
  projectId: 'japri-dev',
  keyFilename: 'C:\\Users\\LENOVO\\Downloads\\RPL\\Code Test\\Simple-App\\japri-dev-a69ba14728d2.json',
});

module.exports = storage;
