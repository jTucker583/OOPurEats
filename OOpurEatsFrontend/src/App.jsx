import { useState } from 'react'
import './App.css'

const API_BASE = 'http://localhost:8080/api'

function App() {
  const [response, setResponse] = useState(null)
  const [loading, setLoading] = useState(false)
  const [orderId, setOrderId] = useState('')
  const [restaurantType, setRestaurantType] = useState('PIZZA_RESTAURANT')
  const [itemName, setItemName] = useState('')
  const [itemPrice, setItemPrice] = useState('')
  const [itemQuantity, setItemQuantity] = useState('1')
  const [orderItems, setOrderItems] = useState([])

  const addItem = () => {
    if (itemName && itemPrice) {
      setOrderItems([...orderItems, {
        name: itemName,
        price: parseFloat(itemPrice),
        description: '',
        quantity: parseInt(itemQuantity) || 1
      }])
      setItemName('')
      setItemPrice('')
      setItemQuantity('1')
    }
  }

  const removeItem = (index) => {
    setOrderItems(orderItems.filter((_, i) => i !== index))
  }

  const fetchAllOrders = async () => {
    setLoading(true)
    try {
      const res = await fetch(API_BASE)
      const data = await res.json()
      setResponse(JSON.stringify(data, null, 2))
    } catch (error) {
      setResponse(`Error: ${error.message}`)
    } finally {
      setLoading(false)
    }
  }

  const fetchOrderById = async () => {
    if (!orderId) return
    setLoading(true)
    try {
      const res = await fetch(`${API_BASE}/${orderId}`)
      if (res.ok) {
        const data = await res.json()
        setResponse(JSON.stringify(data, null, 2))
      } else {
        setResponse(`Error: ${res.status} ${res.statusText}`)
      }
    } catch (error) {
      setResponse(`Error: ${error.message}`)
    } finally {
      setLoading(false)
    }
  }

  const fetchByRestaurant = async (type) => {
    setLoading(true)
    try {
      const res = await fetch(`${API_BASE}/restaurant/${type}`)
      const data = await res.json()
      setResponse(JSON.stringify(data, null, 2))
    } catch (error) {
      setResponse(`Error: ${error.message}`)
    } finally {
      setLoading(false)
    }
  }

  const fetchByStatus = async (status) => {
    setLoading(true)
    try {
      const res = await fetch(`${API_BASE}/status/${status}`)
      const data = await res.json()
      setResponse(JSON.stringify(data, null, 2))
    } catch (error) {
      setResponse(`Error: ${error.message}`)
    } finally {
      setLoading(false)
    }
  }

  const createOrder = async () => {
    if (orderItems.length === 0) {
      setResponse('Error: Please add at least one order item')
      return
    }
    setLoading(true)
    try {
      const orderData = {
        restaurantType,
        orderItems,
        status: 'ORDERED' // Orders always start as ORDERED
      }
      const res = await fetch(API_BASE, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(orderData)
      })
      if (res.ok) {
        setResponse(`Order created! Status: ${res.status}`)
        setOrderItems([])
      } else {
        setResponse(`Error: ${res.status} ${res.statusText}`)
      }
    } catch (error) {
      setResponse(`Error: ${error.message}`)
    } finally {
      setLoading(false)
    }
  }

  const deleteOrder = async () => {
    if (!orderId) return
    setLoading(true)
    try {
      const res = await fetch(`${API_BASE}/${orderId}`, { method: 'DELETE' })
      if (res.ok || res.status === 204) {
        setResponse('Order deleted successfully')
      } else {
        setResponse(`Error: ${res.status} ${res.statusText}`)
      }
    } catch (error) {
      setResponse(`Error: ${error.message}`)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="app">
      <h1>OOPurEats API Tester</h1>
      
      <div className="section">
        <h2>GET Requests</h2>
        <div className="button-group">
          <button onClick={fetchAllOrders} disabled={loading}>
            Get All Orders
          </button>
          <button onClick={fetchOrderById} disabled={loading || !orderId}>
            Get Order by ID
          </button>
          <button onClick={() => fetchByRestaurant('PIZZA_RESTAURANT')} disabled={loading}>
            Get Pizza Orders
          </button>
          <button onClick={() => fetchByRestaurant('STEAK_RESTAURANT')} disabled={loading}>
            Get Steak Orders
          </button>
          <button onClick={() => fetchByRestaurant('PASTA_RESTAURANT')} disabled={loading}>
            Get Pasta Orders
          </button>
          <button onClick={() => fetchByStatus('ORDERED')} disabled={loading}>
            Get ORDERED Status
          </button>
        </div>
        <input
          type="text"
          placeholder="Order ID"
          value={orderId}
          onChange={(e) => setOrderId(e.target.value)}
          style={{ marginTop: '10px', padding: '5px' }}
        />
      </div>

      <div className="section">
        <h2>Create Order</h2>
        <div className="form-group">
          <label>Restaurant Type:</label>
          <select value={restaurantType} onChange={(e) => setRestaurantType(e.target.value)}>
            <option value="PIZZA_RESTAURANT">Pizza</option>
            <option value="STEAK_RESTAURANT">Steak</option>
            <option value="PASTA_RESTAURANT">Pasta</option>
          </select>
        </div>
        <div className="form-group">
          <input
            type="text"
            placeholder="Item name"
            value={itemName}
            onChange={(e) => setItemName(e.target.value)}
          />
          <input
            type="number"
            placeholder="Price"
            value={itemPrice}
            onChange={(e) => setItemPrice(e.target.value)}
            step="0.01"
          />
          <input
            type="number"
            placeholder="Quantity"
            value={itemQuantity}
            onChange={(e) => setItemQuantity(e.target.value)}
          />
          <button onClick={addItem}>Add Item</button>
        </div>
        {orderItems.length > 0 && (
          <div className="order-items">
            <h3>Order Items:</h3>
            {orderItems.map((item, idx) => (
              <div key={idx} className="order-item">
                {item.name} - ${item.price} x{item.quantity}
                <button onClick={() => removeItem(idx)}>Remove</button>
              </div>
            ))}
          </div>
        )}
        <button onClick={createOrder} disabled={loading || orderItems.length === 0}>
          Create Order
        </button>
      </div>

      <div className="section">
        <h2>DELETE Request</h2>
        <button onClick={deleteOrder} disabled={loading || !orderId}>
          Delete Order by ID
        </button>
      </div>

      {loading && <div className="loading">Loading...</div>}

      <div className="section">
        <h2>API Response</h2>
        <pre className="response">{response || 'No response yet'}</pre>
      </div>
    </div>
  )
}

export default App
