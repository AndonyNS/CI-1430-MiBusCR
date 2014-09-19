require 'rails_helper'

RSpec.describe "RutaUsuarios", :type => :request do
  describe "GET /ruta_usuarios" do
    it "works! (now write some real specs)" do
      get ruta_usuarios_path
      expect(response).to have_http_status(200)
    end
  end
end
