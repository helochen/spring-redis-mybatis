package com.chen.threads;

import org.junit.Test;

/**
 * Created by Chen on 2017/6/6.
 */
public class ProductCustomer {

    @Test
    public void testThead() {
        ProductStack ps = new ProductStack();
        Producer p = new Producer(ps, "生产者1");
        Consumer c = new Consumer(ps, "消费者1");
        new Thread(p).start();
        new Thread(c).start();
        while (true){

        }
    }

    class ProductStack {
        int index = 0;

        Product[] arrProduct = new Product[6];

        public synchronized void push(Product product) {
            // 如果仓库满了
            while (index == arrProduct.length) {
                try {
                    System.out.println(product.getProducedBy() + " is waiting.");
                    // 等待，并且从这里退出push()
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(product.getProducedBy() + " sent a notifyAll().");

            // 因为我们不确定有没有线程在wait()，所以我们既然生产了产品，就唤醒有可能等待的消费者，让他们醒来，准备消费
            notifyAll();
            // 注意，notifyAll()以后，并没有退出，而是继续执行直到完成。
            arrProduct[index] = product;
            index++;
            System.out.println(product.getProducedBy() + " 生产了: " + product);
        }

        // pop用来让消费者取出产品的
        public synchronized Product pop(String consumerName) {
            // 如果仓库空了
            while (index == 0) {
                try {
                    System.out.println(consumerName + " is waiting.");
                    // 等待，并且从这里退出pop()
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(consumerName + " sent a notifyAll().");
            // 因为我们不确定有没有线程在wait()，所以我们既然消费了产品，就唤醒有可能等待的生产者，让他们醒来，准备生产
            notifyAll();
            // 注意，notifyAll()以后，并没有退出，而是继续执行直到完成。
            // 取出产品
            index--;
            Product product = arrProduct[index];
            product.consume(consumerName);
            System.out.println(product.getConsumedBy() + " 消费了: " + product);
            return product;
        }
    }

    class Product {
        int id;

        public String getProducedBy() {
            return producedBy;
        }

        public void setProducedBy(String producedBy) {
            this.producedBy = producedBy;
        }

        public String getConsumedBy() {
            return consumedBy;
        }

        public void setConsumedBy(String consumedBy) {
            this.consumedBy = consumedBy;
        }

        private String producedBy = "N/A";

        private String consumedBy = "N/A";

        public Product(int id, String producedBy) {
            this.id = id;
            this.producedBy = producedBy;
        }

        public void consume(String consumedBy) {
            this.consumedBy = consumedBy;
        }

        public String toString() {
            return new StringBuilder().append("Product : ")
                    .append(id).append(", produced by ")
                    .append(producedBy)
                    .append(", consumed by ")
                    .append(consumedBy).toString();
        }
    }


    class Producer implements Runnable {
        String name;

        ProductStack ps = null;

        Producer(ProductStack ps, String name) {
            this.ps = ps;
            this.name = name;
        }

        public void run() {
            for (int i = 0; i < 20; i++) {
                Product product = new Product(i, name);
                ps.push(product);
                try {
                    Thread.sleep((int) (Math.random() * 200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Consumer implements Runnable {
        String name;

        ProductStack ps = null;

        Consumer(ProductStack ps, String name) {
            this.ps = ps;
            this.name = name;
        }

        public void run() {
            for (int i = 0; i < 20; i++) {
                Product product = ps.pop(name);
                try {
                    Thread.sleep((int) (Math.random() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
