const db = require('../config/db');

const addFormJasa = async (req, res) => {
  try {
    const { job_name, start_day, end_day, start_time, end_time, salary, address, description, pengguna_jasa, pemberi_jasa, status, feedback } = req.body;

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
      pemberi_jasa,
      feedback: feedback || [],
      createdAt: new Date(),
    };

    const formJasaRef = await db.collection('formJasa').add(formJasaData);

    res.json({ message: 'Form Jasa added successfully', formJasaId: formJasaRef.id });
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
