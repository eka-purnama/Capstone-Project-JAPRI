const db = require('../config/db');

const addFeedback = async (req, res) => {
   try {
      const { rating, idFormJasa, comment } = req.body;
      const username = req.params.username;

      const feedbacks = {
         rating,
         idFormJasa,
         comment
      };

      // Menambahkan data feedback ke koleksi 'feedbacks'
      const feedbackRef = await db.collection('feedbacks').add(feedbacks);

      // Mengambil referensi dokumen pengguna berdasarkan username
      const userRef = await db.collection('users').where('username', '==', username).get();

      // Memastikan bahwa dokumen pengguna ditemukan
      if (userRef.empty) {
         return res.status(404).json({ message: 'Pengguna tidak ditemukan' });
      }

      // Mendapatkan ID dokumen pengguna
      const userId = userRef.docs[0].id;

      // Membaca data pengguna
      const userData = (await db.collection('users').doc(userId).get()).data();

      // Memperbarui array 'feedbacks' dengan ID feedback baru (jika belum ada)
      const updatedFeedbacks = userData.feedbacks || [];
      updatedFeedbacks.push(feedbackRef.id);

      // Menulis kembali data pengguna dengan array feedback yang diperbarui
      await db.collection('users').doc(userId).update({
         feedbacks: updatedFeedbacks
      });

      res.json({ message: 'Feedback berhasil ditambahkan', feedbackId: feedbackRef.id });

   } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Internal Server Error' });
   }
};

const getRatingUser = async (req, res) => {
   try {
      const username = req.params.username;

      // Mengambil referensi dokumen pengguna berdasarkan username
      const userRef = await db.collection('users').where('username', '==', username).get();

      // Memastikan bahwa dokumen pengguna ditemukan
      if (userRef.empty) {
         return res.status(404).json({ message: 'Pengguna tidak ditemukan' });
      }

      // Mendapatkan array feedback dari dokumen pengguna
      const userFeedback = userRef.docs[0].data().feedbacks || [];

      // Mengambil data feedback berdasarkan ID-feedback dari array userFeedback
      const feedbackDocs = await Promise.all(userFeedback.map(async (feedbackId) => {
         const feedbackDoc = await db.collection('feedbacks').doc(feedbackId).get();
         return feedbackDoc.data();
      }));

      // Menghitung total nilai rating dan jumlah feedback
      let totalRating = 0;
      let totalFeedback = feedbackDocs.length;

      feedbackDocs.forEach((feedback) => {
         totalRating += feedback.rating;
      });

      // Menghitung nilai rata-rata rating (jika ada feedback)
      const averageRating = totalFeedback > 0 ? totalRating / totalFeedback : 0;

      res.json({ message: 'Feedback ditampilkan', totalFeedback: totalFeedback, averageRating: averageRating });

   } catch (error) {
      console.error(error);
      res.status(500).json({ message: 'Internal Server Error' });
   }
};



module.exports = {
   addFeedback,
   getRatingUser,
};
