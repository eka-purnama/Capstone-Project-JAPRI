const db = require('../config/db');

// fungsi menambahkan pekerjaan
const addFormJasa = async (req, res) => {
  try {
    const { job_name, start_day, end_day, start_time, end_time, salary, address, description, feedback, status, pengguna_jasa, penyedia_jasa } = req.body;

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
      pengguna_jasa,
      penyedia_jasa,
      feedback: {},
      created_at: new Date(),
      updated_at: new Date()
    };

    const formJasaRef = await db.collection('formJasa').add(formJasaData);

    res.json({ message: 'Form Jasa added successfully', formJasaId: formJasaRef.id });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

// fungsi merubah status pekerjaan menjadi selesai dan memberikan rating
const updateFormJasaDone = async (req, res) => {
  try {
    const formJasaId = req.params.id;
    const newFeedback = req.body.feedback;
    
    const formJasaData = (await db.collection('formJasa').doc(formJasaId).get()).data();

    if (!formJasaData) {
      return res.status(404).json({ message: 'Form Jasa not found' });
    }

    // Menyiapkan objek feedback yang diperbarui
    const updatedFeedback = {
      ...newFeedback,
    };

    // Memperbarui data formJasa dengan feedback yang diperbarui
    const formJasaRef = await db.collection('formJasa').doc(formJasaId);
    await formJasaRef.update({
      status: 'selesai',
      feedback: updatedFeedback,
      updated_at: new Date()
    });

    res.status(200).json({ message: 'Form Jasa berhasil diupdate'});
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

// fungsi dapatkan riwayat kerja user
const getFormJasaByUser = async (req, res) => {
  try {
    const username = req.params.username;
    const formJasaSnapshot = await db.collection('formJasa')
      .where('penyedia_jasa', '==', username)
      .orderBy('created_at', 'desc')
      .get();

    const formJasaList = formJasaSnapshot.docs.map(
      (doc) => ({ 
        id: doc.id, 
        ...doc.data() 
      })
    );

    res.json({ formJasaList });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

// fungsi dapatkan riwayat kerja sesuai id
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
  getFormJasaByUser,
  getFormJasaById,
};
