class User < ActiveRecord::Base
 has_many :ruta_user
 has_many :ruta, through: :ruta_user

 before_create :generate_token

  private
    def generate_token
      begin
        self.token = SecureRandom.hex.to_s
      end while self.class.exists?(token: token)
    end
end
