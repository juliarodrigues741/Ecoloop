-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: ecopontos
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `beneficios`
--

DROP TABLE IF EXISTS `beneficios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beneficios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descricao` text COLLATE utf8mb4_unicode_ci,
  `categoria` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pontos_necessarios` int NOT NULL,
  `imagem_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beneficios`
--

LOCK TABLES `beneficios` WRITE;
/*!40000 ALTER TABLE `beneficios` DISABLE KEYS */;
INSERT INTO `beneficios` VALUES (1,'RENNER','JFDSKJJKFDSJHKFDS','LOJAS',1500,NULL),(2,'MARISA ','','LOJAS',5000,NULL);
/*!40000 ALTER TABLE `beneficios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `beneficios_resgatados`
--

DROP TABLE IF EXISTS `beneficios_resgatados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beneficios_resgatados` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `beneficio_id` int NOT NULL,
  `data_resgate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `beneficio_id` (`beneficio_id`),
  CONSTRAINT `beneficios_resgatados_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `beneficios_resgatados_ibfk_2` FOREIGN KEY (`beneficio_id`) REFERENCES `beneficios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beneficios_resgatados`
--

LOCK TABLES `beneficios_resgatados` WRITE;
/*!40000 ALTER TABLE `beneficios_resgatados` DISABLE KEYS */;
INSERT INTO `beneficios_resgatados` VALUES (1,1,1,'2025-11-30 14:03:27'),(2,1,2,'2025-11-30 14:07:19');
/*!40000 ALTER TABLE `beneficios_resgatados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracoes_usuario`
--

DROP TABLE IF EXISTS `configuracoes_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracoes_usuario` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `idioma` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'pt-BR',
  `tema_escuro` tinyint(1) DEFAULT '0',
  `notificacao_email` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `configuracoes_usuario_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracoes_usuario`
--

LOCK TABLES `configuracoes_usuario` WRITE;
/*!40000 ALTER TABLE `configuracoes_usuario` DISABLE KEYS */;
INSERT INTO `configuracoes_usuario` VALUES (1,1,'en',1,1);
/*!40000 ALTER TABLE `configuracoes_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conquistas`
--

DROP TABLE IF EXISTS `conquistas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conquistas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descricao` text COLLATE utf8mb4_unicode_ci,
  `pontos_recompensa` int DEFAULT '0',
  `nivel_requerido` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `imagem_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conquistas`
--

LOCK TABLES `conquistas` WRITE;
/*!40000 ALTER TABLE `conquistas` DISABLE KEYS */;
INSERT INTO `conquistas` VALUES (1,'FAÇA A RECICLAGEM DE 5 ITENS','',1000,NULL,NULL),(2,'FAÇA A RECICLAGEM DE 20 ITENS','',2000,NULL,NULL);
/*!40000 ALTER TABLE `conquistas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `desafios`
--

DROP TABLE IF EXISTS `desafios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `desafios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descricao` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `pontos_recompensa` int NOT NULL,
  `nivel_requerido` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `imagem_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `metaKg` double DEFAULT '10',
  `tipo_material` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'geral',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `desafios`
--

LOCK TABLES `desafios` WRITE;
/*!40000 ALTER TABLE `desafios` DISABLE KEYS */;
INSERT INTO `desafios` VALUES (1,'Reciclagem de Plástico','Separe corretamente resíduos plásticos por 7 dias.',50,'Iniciante','img/desafios/plastico.png',10,'geral'),(2,'Economia de Água','Reduza o consumo de água por uma semana.',70,'Iniciante','img/desafios/agua.png',10,'geral'),(3,'Caminhada Sustentável','Evite usar carro por 3 dias e caminhe ou use bicicleta.',40,'Iniciante','img/desafios/caminhada.png',10,'geral'),(4,'Energia Limpa','Fique 24h sem usar dispositivos eletrônicos desnecessários.',80,'Intermediário','img/desafios/energia.png',10,'geral'),(5,'Mutirão de Limpeza','Participe de uma ação de limpeza em um parque ou praia.',120,'Avançado','img/desafios/limpeza.png',10,'geral'),(6,'Compostagem Doméstica','Inicie um sistema de compostagem em casa.',100,'Intermediário','img/desafios/compostagem.png',10,'geral'),(7,'Dia Sem Plástico','Passe um dia inteiro sem usar plástico descartável.',60,'Iniciante','img/desafios/semplastico.png',10,'geral'),(8,'Consumo Consciente','Faça compras sem adquirir produtos com embalagens desnecessárias.',90,'Intermediário','img/desafios/consumo.png',10,'geral'),(9,'Plantio de Árvore','Plante ao menos uma árvore em local permitido.',150,'Avançado','img/desafios/arvore.png',10,'geral'),(10,'Primeiros 5kg','Recicle ao menos 5kg de materiais para concluir este desafio.',50,NULL,'img/desafios/5kg.png',5,'geral'),(11,'Pequeno Reciclador','Recicle 10kg de materiais. Continue assim!',120,NULL,'img/desafios/10kg.png',10,'geral'),(12,'Eco Guerreiro','Envie um total de 25kg de materiais e ganhe muitos pontos.',300,NULL,'img/desafios/25kg.png',25,'geral'),(13,'Herói da Reciclagem','Alcance a marca de 50kg reciclados. Você está fazendo a diferença!',600,NULL,'img/desafios/50kg.png',50,'geral'),(14,'Lenda Sustentável','O maior desafio: Recicle 100kg! Somente os melhores chegam aqui.',1500,NULL,'img/desafios/100kg.png',100,'geral'),(15,'Primeiros 5kg','Recicle ao menos 5kg de materiais para concluir este desafio.',50,NULL,'img/desafios/5kg.png',5,'geral'),(16,'Pequeno Reciclador','Recicle 10kg de materiais. Continue assim!',120,NULL,'img/desafios/10kg.png',10,'geral'),(17,'Eco Guerreiro','Envie um total de 25kg de materiais e ganhe muitos pontos.',300,NULL,'img/desafios/25kg.png',25,'geral'),(18,'Herói da Reciclagem','Alcance a marca de 50kg reciclados. Você está fazendo a diferença!',600,NULL,'img/desafios/50kg.png',50,'geral'),(19,'Lenda Sustentável','O maior desafio: Recicle 100kg! Somente os melhores chegam aqui.',1500,NULL,'img/desafios/100kg.png',100,'geral');
/*!40000 ALTER TABLE `desafios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `logs_acoes`
--

DROP TABLE IF EXISTS `logs_acoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `logs_acoes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int DEFAULT NULL,
  `acao` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `descricao` text COLLATE utf8mb4_unicode_ci,
  `data_acao` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `logs_acoes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `logs_acoes`
--

LOCK TABLES `logs_acoes` WRITE;
/*!40000 ALTER TABLE `logs_acoes` DISABLE KEYS */;
/*!40000 ALTER TABLE `logs_acoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materiais_enviados`
--

DROP TABLE IF EXISTS `materiais_enviados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materiais_enviados` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `descricao` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo_arquivo` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `caminho_arquivo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `data_envio` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `pontos_gerados` int DEFAULT '0',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'pendente',
  `data_avaliacao` timestamp NULL DEFAULT NULL,
  `comentario_avaliacao` text COLLATE utf8mb4_unicode_ci,
  `tipo_material` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `peso_kg` double DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `materiais_enviados_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `materiais_enviados_chk_1` CHECK ((`tipo_arquivo` in (_utf8mb4'foto',_utf8mb4'video'))),
  CONSTRAINT `materiais_enviados_chk_2` CHECK ((`status` in (_utf8mb4'pendente',_utf8mb4'aprovado',_utf8mb4'recusado')))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materiais_enviados`
--

LOCK TABLES `materiais_enviados` WRITE;
/*!40000 ALTER TABLE `materiais_enviados` DISABLE KEYS */;
INSERT INTO `materiais_enviados` VALUES (1,1,'ddddddddddddddfdas','foto','/uploads/9d977077eb827892653d2f8625f3ebf4562ef660v2_hq.jpg','2025-11-29 16:10:05',2500,'aprovado','2025-11-30 04:30:57','Aprovado pelo administrador','',2.5),(2,1,'sdaaaaaaaaaaaaaaaa','foto','/uploads/9d977077eb827892653d2f8625f3ebf4562ef660v2_hq.jpg','2025-11-29 21:55:34',NULL,'recusado','2025-11-30 04:31:15','INCORRETO','',3),(3,1,'SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS','foto','/uploads/1764458327299_9d977077eb827892653d2f8625f3ebf4562ef660v2_hq.jpg','2025-11-29 23:18:47',8500,'aprovado','2025-11-30 04:31:22','Aprovado pelo administrador','Vidro',4.5),(4,1,'sddddddddddddddddddddd','foto','/uploads/1764550376550_9d977077eb827892653d2f8625f3ebf4562ef660v2_hq.jpg','2025-12-01 00:52:57',5000,'aprovado','2025-12-01 00:55:35','Aprovado pelo administrador','Plástico',9.5),(5,1,'','foto','/uploads/1764627296446_9d977077eb827892653d2f8625f3ebf4562ef660v2_hq.jpg','2025-12-01 22:14:56',2500,'aprovado','2025-12-01 22:16:18','Aprovado pelo administrador','Vidro',10.5),(6,1,'','foto','/uploads/1764627874802_9d977077eb827892653d2f8625f3ebf4562ef660v2_hq.jpg','2025-12-01 22:24:35',2500,'aprovado','2025-12-01 23:40:05',NULL,'Papel',NULL);
/*!40000 ALTER TABLE `materiais_enviados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificacoes`
--

DROP TABLE IF EXISTS `notificacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificacoes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `mensagem` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lida` tinyint(1) DEFAULT '0',
  `data_envio` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `notificacoes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificacoes`
--

LOCK TABLES `notificacoes` WRITE;
/*!40000 ALTER TABLE `notificacoes` DISABLE KEYS */;
INSERT INTO `notificacoes` VALUES (1,1,'Seu envio foi aprovado e você recebeu 2500 pontos!','sistema',0,'2025-11-30 04:30:57'),(2,1,'Seu envio foi recusado: INCORRETO','sistema',0,'2025-11-30 04:31:15'),(3,1,'Seu envio foi aprovado e você recebeu 8500 pontos!','sistema',0,'2025-11-30 04:31:22'),(4,1,'Seu envio foi aprovado e você recebeu 5000 pontos!','sistema',0,'2025-12-01 00:55:35'),(5,1,'Seu envio foi aprovado e você recebeu 2500 pontos!','sistema',0,'2025-12-01 22:16:18'),(6,1,'Seu envio foi aprovado e você recebeu 2500 pontos!','sistema',0,'2025-12-01 23:40:05');
/*!40000 ALTER TABLE `notificacoes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ranking_local`
--

DROP TABLE IF EXISTS `ranking_local`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ranking_local` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `posicao` int DEFAULT NULL,
  `pontos` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `ranking_local_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ranking_local`
--

LOCK TABLES `ranking_local` WRITE;
/*!40000 ALTER TABLE `ranking_local` DISABLE KEYS */;
INSERT INTO `ranking_local` VALUES (1,1,1,100);
/*!40000 ALTER TABLE `ranking_local` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_desafios`
--

DROP TABLE IF EXISTS `usuario_desafios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_desafios` (
  `usuario_id` int NOT NULL,
  `desafio_id` int NOT NULL,
  `data_conclusao` datetime DEFAULT NULL,
  `progresso` double DEFAULT '0',
  PRIMARY KEY (`usuario_id`,`desafio_id`),
  KEY `desafio_id` (`desafio_id`),
  CONSTRAINT `usuario_desafios_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `usuario_desafios_ibfk_2` FOREIGN KEY (`desafio_id`) REFERENCES `desafios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_desafios`
--

LOCK TABLES `usuario_desafios` WRITE;
/*!40000 ALTER TABLE `usuario_desafios` DISABLE KEYS */;
INSERT INTO `usuario_desafios` VALUES (1,1,NULL,0),(1,2,NULL,0),(1,3,NULL,0),(1,4,NULL,0),(1,5,NULL,0),(1,6,NULL,0),(1,7,NULL,0),(1,8,NULL,0),(1,9,NULL,0);
/*!40000 ALTER TABLE `usuario_desafios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `senha_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `foto_perfil` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nivel` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'Bronze',
  `pontos` int DEFAULT '0',
  `data_cadastro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `role` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Usuário Teste','user@teste.com','123456','/uploads/1764619093119_9d977077eb827892653d2f8625f3ebf4562ef660v2_hq.jpg','Prata',14500,'2025-11-27 23:44:45','USER'),(2,'Admin','admin@eco.com','123',NULL,'Bronze',0,'2025-11-28 00:41:28','ADMIN');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios_conquistas`
--

DROP TABLE IF EXISTS `usuarios_conquistas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios_conquistas` (
  `usuario_id` int NOT NULL,
  `conquista_id` int NOT NULL,
  `data_conquista` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`usuario_id`,`conquista_id`),
  KEY `conquista_id` (`conquista_id`),
  CONSTRAINT `usuarios_conquistas_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `usuarios_conquistas_ibfk_2` FOREIGN KEY (`conquista_id`) REFERENCES `conquistas` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios_conquistas`
--

LOCK TABLES `usuarios_conquistas` WRITE;
/*!40000 ALTER TABLE `usuarios_conquistas` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuarios_conquistas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-01 21:44:38
