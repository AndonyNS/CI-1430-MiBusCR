json.array!(@ruta_paradas) do |ruta_parada|
  json.extract! ruta_parada, :id, :ruta_id, :parada_id, :tipo
  json.url ruta_parada_url(ruta_parada, format: :json)
end
