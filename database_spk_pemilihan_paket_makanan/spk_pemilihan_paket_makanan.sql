-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 29, 2025 at 05:09 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `spk_pemilihan_paket_makanan`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `id` char(3) NOT NULL,
  `namalengkap` varchar(20) DEFAULT NULL,
  `user` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `namalengkap`, `user`, `password`) VALUES
('001', 'Neubri Hidayah', 'admin', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `alternatif`
--

CREATE TABLE `alternatif` (
  `id_layanan` varchar(5) NOT NULL,
  `nama_layanan` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `alternatif`
--

INSERT INTO `alternatif` (`id_layanan`, `nama_layanan`) VALUES
('01', 'Shopeefod'),
('02', 'Grabfood'),
('03', 'Gofood');

-- --------------------------------------------------------

--
-- Table structure for table `kriteria`
--

CREATE TABLE `kriteria` (
  `kd_kriteria` char(3) NOT NULL,
  `nama_kriteria` varchar(30) NOT NULL,
  `nilai_prioritas` decimal(4,3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kriteria`
--

INSERT INTO `kriteria` (`kd_kriteria`, `nama_kriteria`, `nilai_prioritas`) VALUES
('K1', 'Biaya layanan', '0.000'),
('K2', 'Popularitas pengguna', '0.000'),
('K3', 'Kemudahan sistem', '0.000'),
('K4', 'Fitur promosi', '0.000'),
('K5', 'Kemudahan transaksi', '0.000');

-- --------------------------------------------------------

--
-- Table structure for table `register`
--

CREATE TABLE `register` (
  `id` int(3) NOT NULL,
  `email` varchar(50) NOT NULL,
  `user` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `register`
--

INSERT INTO `register` (`id`, `email`, `user`, `password`) VALUES
(1, 'mail@gmail.com', 'admin', 'admin'),
(2, '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `seleksi`
--

CREATE TABLE `seleksi` (
  `id_layanan` char(3) NOT NULL,
  `nama_layanan` varchar(30) NOT NULL,
  `hasil_penilaian` decimal(4,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `alternatif`
--
ALTER TABLE `alternatif`
  ADD PRIMARY KEY (`id_layanan`);

--
-- Indexes for table `kriteria`
--
ALTER TABLE `kriteria`
  ADD PRIMARY KEY (`kd_kriteria`);

--
-- Indexes for table `register`
--
ALTER TABLE `register`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `seleksi`
--
ALTER TABLE `seleksi`
  ADD UNIQUE KEY `id_paket` (`id_layanan`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `register`
--
ALTER TABLE `register`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
