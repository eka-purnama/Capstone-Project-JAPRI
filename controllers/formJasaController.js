const db = require('../config/db');

const addFormJasa = async (req, res) => {
  try {
    const { job_name, start_day, end_day, start_time, end_time, salary, address, description, pengguna_jasa, penyedia_jasa, status } = req.body;
    const usernamePenggunaJasa = req.params.usernamepengguna;
    const usernamePenyediaJasa = req.params.usernamepenyedia;

    const formJasaData = {
      job_name,
      start_day,
      end_day,
      start_time,
      end_time,
      salary,
      address,
      description,
      status,
      pengguna_jasa:usernamePenggunaJasa,
      penyedia_jasa:usernamePenyediaJasa,
      createdAt: new Date(),
    };

    const formJasaRef = await db.collection('formJasa').add(formJasaData);

    res.json({ message: 'Form Jasa added successfully', formJasaId: formJasaRef.id });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};


const getFormJasaByUser = async (req, res) => {
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









const getAllFormJasa = async (req, res) => {
  try {
    const formJasaSnapshot = await db.collection('formJasa').get();
    const formJasaList = formJasaSnapshot.docs.map((doc) => ({ id: doc.id, ...doc.data() }));

    res.json({ formJasaList });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

const getFormJasaById = async (req, res) => {
  try {
    const formJasaId = req.params.id;
    const formJasaDoc = await db.collection('formJasa').doc(formJasaId).get();

    if (!formJasaDoc.exists) {
      return res.status(404).json({ message: 'Form Jasa not found' });
    }

    const formJasaData = formJasaDoc.data();

    res.json({ formJasaData });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

module.exports = {
  addFormJasa,
  getAllFormJasa,
  getFormJasaById,
};
