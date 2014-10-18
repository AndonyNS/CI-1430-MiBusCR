json.array!(@eventos) do |evento|
  json.extract! evento, :id, :Descripcion, :Tipo, :Hora, :Lugar
  json.url evento_url(evento, format: :json)
end
