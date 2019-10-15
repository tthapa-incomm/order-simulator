```http request
POST http://localhost:7007/tools/b2b-orders/complete
Content-Type: application/json

["v0010.VBOL-v0010","e0009.BOL-e0009"]
```

```http request
curl -d '["v0010.VBOL-v0010","e0009.BOL-e0009"]' -X POST -H "Content-Type: application/json"  http://localhost:7007/tools/b2b-orders/complete
```

To open port:

``
iptables -A IN_public_allow -p tcp --dport 7007 -m conntrack --ctstate NEW -j ACCEPT
``