CREATE TABLE `products` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NULL,
  `created` DATETIME NULL DEFAULT NOW(),
  PRIMARY KEY (`id`));

  ALTER TABLE `products`
  ADD COLUMN `price` DOUBLE NOT NULL AFTER `name`,
  ADD COLUMN `updated` DATETIME NULL AFTER `price`,
  CHANGE COLUMN `name` `name` VARCHAR(200) NOT NULL ,
  CHANGE COLUMN `created` `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;

  CREATE TABLE `orders` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `status` VARCHAR(45) NOT NULL DEFAULT 'NEW',
    `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`));

  CREATE TABLE `order_items` (
    `order_id` INT NOT NULL,
    `product_id` INT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    PRIMARY KEY (`order_id`, `product_id`),
    INDEX `fk_order_items_products_idx` (`product_id` ASC) VISIBLE,
    CONSTRAINT `fk_order_items_orders`
      FOREIGN KEY (`order_id`)
      REFERENCES `shopping`.`orders` (`id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION,
    CONSTRAINT `fk_order_items_products`
      FOREIGN KEY (`product_id`)
      REFERENCES `shopping`.`products` (`id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);

