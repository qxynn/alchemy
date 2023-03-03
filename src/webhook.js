const { Webhook, MessageBuilder } = require('discord-webhook-node');
const hook = new Webhook(" ******* ");


/*
const embed = new MessageBuilder()
embed.setTitle("title")
embed.setAuthor("author")
embed.setURL("http://qxynn.com")
embed.setDescription("description")
embed.setTimestamp()
embed.setFooter("footer")
*/

const bought = new MessageBuilder()
bought.setTitle("BOUGHT")
//embed.setURL("http://qxynn.com")
//embed.setDescription("description")
bought.setTimestamp()
bought.setFooter("ALCHEMY | TOTAL PROFIT | $4,444")


const sold = new MessageBuilder()
sold.setTitle("SOLD | $12.50")
sold.setColor('#AFE1AF')
//embed.setURL("http://qxynn.com")
sold.setDescription("open 4235.25 | closed 4235.5")
sold.setTimestamp()
sold.setFooter("ALCHEMY | TOTAL PROFIT | $4,444")


const sold2 = new MessageBuilder()
sold2.setTitle("SOLD | -$12.50")
sold2.setColor('#880808')
//embed.setURL("http://qxynn.com")
sold2.setDescription("open 4235.25 | closed 4235")
sold2.setTimestamp()
sold2.setFooter("ALCHEMY | TOTAL PROFIT | $4,444")


hook.send(bought)
hook.send(sold)
hook.send(sold2)
