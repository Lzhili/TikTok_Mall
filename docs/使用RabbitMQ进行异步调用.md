# RabbitMQ的应用场景

1. PaymentService支付成功后，使用MQ异步通知OrderService的标记订单为已支付的业务
2. OrderService创建并提交订单后，会发送一个延迟消息到延迟队列中，延迟队列中的消息会在一段时间后被OrderService消费，通知OrderService处理以下情况：

   (1) OrderService提交订单后并在一段时间内支付，PaymentService使用MQ异步通知OrderService的标记订单为已支付（收到MQ延迟消息，发现订单已支付，不做任何操作）。

   (2) OrderService提交订单后不支付，超过一段时间后，订单超时自动取消（收到MQ延迟消息，订单定时取消）。

   (3) OrderService提交订单后并在一段时间内支付，尽管有发送者，MQ以及消费者的可靠性措施，但PaymentService的MQ消息仍然可能发送不到OrderService，所以需要一个兜底方案，
   兜底方案会收到MQ延迟消息，检查并确保Order和Payment支付状态的一致性。

