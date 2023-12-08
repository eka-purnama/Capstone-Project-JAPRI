const db = require('../config/db');

const addFormJasa = async (req, res) => {
  try {
    const { job_name, start_day, end_day, start_time, end_time, salary, address, description, feedbacks, status } = req.body;
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
      feedbacks:[],
      createdAt: new Date(),
    };

    const formJasaRef = await db.collection('formJasa').add(formJasaData);

    res.json({ message: 'Form Jasa added successfully', formJasaId: formJasaRef.id });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

const updateFormJasaDone = async (req, res) => {
  try {
    const formJasaId = req.params.id; // ambil id
    const feedbacks = req.body;

    
    const formJasaData = (await db.collection('formJasa').doc(formJasaId).get()).data();

    const updatedFeedbacks = formJasaData.feedbacks || [];
      updatedFeedbacks.push(feedbacks);

      // Menulis kembali data pengguna dengan array feedback yang diperbarui
      const formJasaRef = await db.collection('formJasa').doc(formJasaId);
      await formJasaRef.update({
        status: "selesai", 
        feedbacks: updatedFeedbacks
      });

      res.status(200).json({ message: 'Form Jasa berhasil diupdate' });

  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};


const getRatingUser = async (req, res) => {
  try {
    const username = req.params.username;

    // Mengambil referensi dokumen pengguna berdasarkan username
    const userRef = await db.collection('formJasa').where('penyedia_jasa', '==', username).get();

    let totalRating = 0;
    let totalFeedback = 0;

    // Menghitung nilai total rating dan jumlah feedback
    userRef.forEach((doc) => {
      const feedbackData = doc.data();

      // Periksa apakah ada feedback di dalam array feedbacks
      if (feedbackData.feedbacks && feedbackData.feedbacks.length > 0) {
        // Ambil hanya feedback pada array pertama
        const firstFeedback = feedbackData.feedbacks[0];
        
        totalRating += firstFeedback.rating;
        totalFeedback++;
      }
    });

    // Menghitung nilai rata-rata rating (jika ada feedback) dan membatasi ke 1 angka dibelakang koma
    const averageRating = totalFeedback > 0 ? parseFloat((totalRating / totalFeedback).toFixed(1)) : false;

    res.json({ message: 'Feedback ditampilkan', totalFeedback: totalFeedback, averageRating: averageRating });

  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

const getFormJasaByUser = async (req, res) => {
  try {
    const username = req.params.username;
    // Mengambil referensi dokumen berdasarkan username
    const formJasaSnapshot = await db.collection('formJasa').where('penyedia_jasa', '==', username).get();
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
  updateFormJasaDone,
  getRatingUser,
  getFormJasaByUser,
  getFormJasaById,
};
