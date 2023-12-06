# Menggunakan Node.js versi 14 sebagai base image
FROM node:14

# Membuat dan mengarahkan direktori kerja di dalam container
WORKDIR /app

# Menyalin package.json dan package-lock.json untuk instalasi dependensi
COPY package*.json ./

# Menginstal dependensi
RUN npm install

# Menyalin seluruh kode sumber proyek ke dalam container
COPY . .

# Menjalankan aplikasi saat container dimulai
CMD ["node", "index.js"]

