// pada postman > headers > add Content-Type = application/json

const express = require("express");
// npm install express body-parser
const bodyParser = require("body-parser");
// npm install express body-parser @google-cloud/firestore
const { Firestore } = require("@google-cloud/firestore");
// npm install lodash
const _ = require('lodash');
// npm install @google-cloud/storage
// cloud storage buckets > permissons > access control = fine-grained
const { Storage } = require('@google-cloud/storage');
// npm install bcrypt
const bcrypt = require('bcrypt');


const path = require('path');
const url = require('url');



const app = express();
const PORT = 3000;

// Initialize Firestore with your project ID and key file
const firestore = new Firestore({
projectId: "japri-dev",
keyFilename: "F:\\BANGKIT\\Capstone\\japri\\japri-dev-firestore.json",
});

const storage = new Storage({
   projectId: 'japri-dev',
   keyFilename: 'F:\\BANGKIT\\Capstone\\japri\\japri-dev-cloud-storage.json',
});

// Referensi ke bucket
const bucket = storage.bucket('japri-dev-bucket');

// Firestore collection reference
const usersCollection = firestore.collection("users");

// Middleware for parsing JSON in the request body
app.use(bodyParser.json());

// Get all users
app.get("/users", async (req, res) => {
   try {
      const usersSnapshot = await usersCollection.get();
      const users = usersSnapshot.docs.map((doc) => ({
      id: doc.id,
      ...doc.data(),
      }));
      res.json(users);
   } catch (error) {
      console.error("Error fetching users:", error);
      res.status(500).json({ error: "Internal server error" });
   }
});

app.get('/users/:id', async (req, res) => {
   const userId = req.params.id;

   try {
      const userSnapshot = await usersCollection.doc(userId).get();

      if (!userSnapshot.exists) {
         return res.status(404).json({ error: 'User not found' });
      }

      const user = { id: userSnapshot.id, ...userSnapshot.data() };
      res.json(user);
   } catch (error) {
      console.error('Error fetching user:', error);
      res.status(500).json({ error: 'Internal server error' });
   }
});


// Endpoint untuk mengupdate data pengguna
app.put('/users/:id', async (req, res) => {
   const userId = req.params.id;
   const updatedUserData = req.body;
   

   try {
      const userRef = usersCollection.doc(userId);
      const userSnapshot = await userRef.get();

      // cek apakah pengguna sudah ada atau belum
      if (!userSnapshot.exists) {
         return res.status(404).json({ error: 'Pengguna tidak ditemukan' });
      }

      // Memastikan bahwa ada setidaknya satu kolom yang diubah
      let isAnyFieldUpdated = false;
      Object.keys(updatedUserData).forEach(field => {
         if (JSON.stringify(userSnapshot.data()[field]) !== JSON.stringify(updatedUserData[field])) {
            isAnyFieldUpdated = true;
         }
      });
      if (!isAnyFieldUpdated) {
         return res.status(400).json({ error: 'Setidaknya satu kolom harus diubah' });
      }

      // Validasi penulisan email
      if (updatedUserData.email) {
         const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
         if (!emailRegex.test(updatedUserData.email)) {
            return res.status(400).json({ error: 'Penulisan Email tidak valid' });
         }
      }

      // lakukan ketika kolom photo_url dirubah
      if (updatedUserData.photo_url) {
         // Dapatkan URL lama
         const oldPhotoUrl = userSnapshot.data().photo_url;
         
         // ambil extensi file
         const fileExtension = path.extname(updatedUserData.photo_url);
         // buat nama file baru dengan uasername dan timestamp
         const username = userSnapshot.data().username;
         const timestamp = Date.now();
         const newFileName = `${username}-userprofile-${timestamp}${fileExtension}`;
         // Mengunggah file ke GCS
         await uploadFile(updatedUserData.photo_url, newFileName);
         // Mengubah photo_url menjadi URL publik ke file GCS
         updatedUserData.photo_url = `https://storage.googleapis.com/${bucket.name}/${newFileName}`;

         // Hapus file lama dari GCS jika ada
         if (oldPhotoUrl) {
            // Ekstrak nama file dari URL
            const fileName = path.basename(url.parse(oldPhotoUrl).pathname);
            // Dapatkan referensi ke file
            const file = bucket.file(fileName);
            // Cek apakah file tersebut ada
            const [exists] = await file.exists();
            // Jika file tersebut ada, hapus
            if (exists) {
               await file.delete();
            }
         }
      }

      // Validasi password
      if (updatedUserData.password) {
         const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$/;
         if (!passwordRegex.test(updatedUserData.password)) {
            return res.status(400).json({
               error: 'Password tidak valid. Password harus minimal 8 karakter dan mengandung huruf besar, huruf kecil, dan angka.'
            });
         }
         // Encrypt password before updating
         const encryptedPassword = await bcrypt.hash(updatedUserData.password, 10);
         updatedUserData.password = encryptedPassword;
      }
      
      // membuat username tidak bisa dirubah
      delete updatedUserData.username;


      // const password = 'password123';
      // const hashedPassword = '$2a$12$1234567890123456789012345678901234567890123456789012345678901234';
      // const isPasswordMatch = bcrypt.compare(password, hashedPassword);
      // console.log(isPasswordMatch);


      // Menggunakan update untuk mengupdate bidang-bidang tertentu
      await userRef.update(updatedUserData);

      // Mengambil dan mengembalikan data pengguna yang sudah diupdate
      const updatedUserSnapshot = await userRef.get();
      const updatedUser = { id: updatedUserSnapshot.id, ...updatedUserSnapshot.data() };
      res.json(updatedUser);
   } catch (error) {
      console.error('Error updating user data:', error);
      res.status(500).json({ error: 'Kesalahan server internal' });
   }
});


async function uploadFile(imgFile, newFileName) {
   await bucket.upload(imgFile, {
      // Make the file publicly accessible
      public: true,

      // Set metadata, such as content type
      metadata: {
         contentType: 'image/jpeg',
      },

      // Specify the new filename
      destination: newFileName,
   });

   console.log(`${imgFile} uploaded to ${bucket.name} as ${newFileName}.`);
}




// Start the server
app.listen(PORT, () => {
console.log(`Server is running on http://localhost:${PORT}`);
});